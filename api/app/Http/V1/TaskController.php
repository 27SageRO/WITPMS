<?php

namespace App\Http\Controllers\V1;

use App\Http\Controllers\Controller;
use DB;
use Response;
use Request;
use DateTime;
use DateTimeZone;

class TaskController extends Controller {
    
	/**
     * Task filtering.
     *
     * @return Json
     */
	public function filter() {
		
		$userId			= Request::input('userId');
		$suggestion 	= Request::input('suggestion');
		$offset 		= Request::input('offset');
		$factor 		= Request::input('factor');
		$types 			= Request::input('types');
		$statuses 		= Request::input('statuses');

		$tasks = $this->taskQueryBuilder($offset, $userId);

		// For View By
		if ($suggestion == 'My tasks') {

			$tasks->where('task.to_users', $factor);

		} else if ($suggestion == 'Created by me') {

			$tasks->where('task.from_user', $factor);

		} else if ($suggestion == 'Completed last 3 days') {
			
			$tasks->where('task.status', 'Completed');
			$tasks->where('task.date_modified', '<', date('Y-m-d H:i:s', strtotime('-3 days')));

		} else if ($suggestion == 'Project') {

			$tasks->where('task.project_id', $factor);

		} else if ($suggestion == 'In Progress') {

			$tasks->where('task.status', $suggestion);
			
		}

		// For task type
		if (count($types) > 0) {
			$tasks->whereIn('task.type', $types);
		}

		// For task status
		if (count($statuses) > 0) {
			$tasks->whereIn('task.status', $statuses);
		}

		$tasks = $tasks->get();

		$this->customizeTasks($tasks);
		$this->fixDates($tasks, $userId);
		return Response::json($tasks);

	}

	/**
     * Get tasks.
     *
     * @param  int $offset
     * @param  int $userId
     * @return Json
     */
	public function getTasks($offset, $userId) {
		$tasks = $this->taskQueryBuilder($offset, $userId)->get();
		$this->customizeTasks($tasks);
		$this->fixDates($tasks, $userId);
		return Response::json($tasks);
	}

	/**
     * Get tasks by ticket id.
     *
     * @param  int $offset
     * @param  int $userId
     * @param  int $ticketId
     * @return Json
     */
	public function getTasksByTicket($offset, $userId, $ticketId) {
		$tasks = $this->taskQueryBuilder($offset, $userId);
		$tasks->where('task.ticket_id', $ticketId);
		$tasks = $tasks->get();
		$this->customizeTasks($tasks);
		$this->fixDates($tasks, $userId);
		return Response::json($tasks);
	}

	/**
     * Get tasks by id.
     *
     * @param  int $taskId
     * @return Json
     */
	public function getTaskById($userId, $taskId) {

		$task = DB::table( 'ticket_logs as task' )
			->leftJoin('projects as project', 'project.id', 'task.project_id')
			->leftJoin('tickets as ticket', 'ticket.id', 'task.ticket_id')
			->select(
				'task.id',
				'task.parent_id',
				'task.ticket_id',
				'task.series_no',
				'task.date_added',
				'task.date_due',
				'task.date_modified',
				'task.description',
				'task.private_note',
				'task.attachments',
				'task.type',
				'task.status',
				'task.from_user',
				'task.to_users',
				'task.project_id',
				'ticket.title as ticket_title',
				'project.name as project_name')
			->where('task.id', $taskId)
			->first(); 

		if (!is_numeric($task->to_users)) {

			# if there are many assignee

			$arr = [];

			$task->to_users = unserialize($task->to_users);

			foreach ($task->to_users as $tempArr) {
				$arr[] = $this->getUser($tempArr['user_id']);
			}

			$task->to_users = $arr;

		} else {

			# if it is single assignee

			$user = $this->getUser($task->to_users);
			$task->to_users = [$user];

		}

		$task->from_user = $this->getUser ($task->from_user);

		# Number of comments
		if ($numberOfComments = DB::table( 'ticket_logs as comment' )
			->select(DB::raw('count(comment.id) as number_of_comments'))
			->where('parent_id', '=', $task->series_no)
			->first()
			->number_of_comments) {
			$task->number_of_comments = $numberOfComments;
		} else {
			$task->number_of_comments = 0;
		}

		$timezone = DB::table('users')
			->leftJoin('zone', 'zone.zone_id', 'users.zone_id')
			->select('zone.zone_name')
			->where('users.id', $userId)
			->first()
			->zone_name;

		if (isset($task->date_added)) {

			$dt = DateTime::createFromFormat('Y-m-d H:i:s', $task->date_added);
			$dt->setTimezone(new DateTimeZone($timezone));
			$task->date_added = $dt->format('Y-m-d H:i:s');

		}

		if (isset($task->date_modified)) {

			$dt = DateTime::createFromFormat('Y-m-d H:i:s', $task->date_modified);
			$dt->setTimezone(new DateTimeZone($timezone));
			$task->date_modified = $dt->format('Y-m-d H:i:s');
			
		}

		if (isset($task->date_due)) {

			$dt = DateTime::createFromFormat('Y-m-d H:i:s', $task->date_due);
			$dt->setTimezone(new DateTimeZone($timezone));
			$task->date_due = $dt->format('Y-m-d H:i:s');
			
		}

		return Response::json($task);

	}

	/**
     * Update a task.
     *
     * @return Json
     */
	public function deleteTask() {

		$id = Request::input('id');

		DB::table('ticket_logs')
			->where('id', $id)
			->delete();

		return Response::json(['message' => 'success']);

	}

	/**
     * Update task description.
     *
     * @return Json
     */
	public function updateTaskDescription() {

		$id 			= Request::input('id');
		$description 	= Request::input('description');
		$now 			= date('Y-m-d H:i:s'); 

		DB::table('ticket_logs')
			->where('id', $id)
			->update([
				'description' => $description,
				'date_modified' => $now
			]);

		return Response::json(['message' => 'success']);

	}

	/**
     * Update task type.
     *
     * @return Json
     */
	public function updateTaskType() {

		$id 	= Request::input('id');
		$type 	= Request::input('type');
		$now 	= date('Y-m-d H:i:s'); 

		DB::table('ticket_logs')
			->where('id', $id)
			->update([
				'type' => $type,
				'date_modified' => $now
			]);

		return Response::json(['message' => 'success']);
	}

	/**
     * Update task date due.
     *
     * @return Json
     */
	public function updateTaskDue() {

		$id 	= Request::input('id');
		$userId = Request::input('userId');
		$date 	= strtotime(Request::input('date'));
		$now 	= date('Y-m-d H:i:s');

		$timezone = DB::table('users')
			->leftJoin('zone', 'zone.zone_id', 'users.zone_id')
			->select('zone.zone_name')
			->where('users.id', $userId)
			->first()
			->zone_name;

		$dt = new DateTime();
		$dt->setTimezone(new DateTimeZone($timezone));
		$dt->setTimestamp($date);
		$date = $dt->format('Y-m-d H:i:s');

		DB::table('ticket_logs')
			->where('id', $id)
			->update([
				'date_due' => $date,
				'date_modified' => $now
			]);

		return Response::json(['message' => 'success']);

	}

	/**
     * Update task assignees.
     *
     * @return Json
     */
	public function updateTaskAssignees() {

		$id 		= Request::input('id');
		$toUsers 	= Request::input('to_users');
		$now 		= date('Y-m-d H:i:s');

		if (count($toUsers) > 1) {
			$tempArr = [];
			foreach ($toUsers as $value) {
				$tempArr[] = ['user_id' => $value, 'type' => 1];
			}
			$toUsers = serialize($tempArr);
		} else {
			$toUsers = $toUsers[0];
		}

		DB::table('ticket_logs')
			->where('id', $id)
			->update([
				'to_users' => $toUsers,
				'date_modified' => $now
			]);

		return Response::json(['message' => 'success']);

	}

	/**
     * Update task status.
     *
     * @return Json
     */
	public function updateTaskStatus() {

		$id 	= Request::input('id');
		$userId = Request::input('userId');
		$status = Request::input('status');
		$now 	= date('Y-m-d H:i:s');

		DB::table('ticket_logs')
			->where('id', $id)
			->update([
				'status' => $status,
				'date_modified' => $now
			]);

		// Get other data from parent
		$task = DB::table('ticket_logs')
			->where('id', $id)
			->first();

		$seriesNo		= $task->series_no;
		$type 			= 'Comment';
		$comment 		= 'Status changed to <b>' . $status . '</b>.';

		// Insert comment
		DB::table( 'ticket_logs' )
			->insert([
				'from_user' => $userId,
				'description' => $comment,
				'parent_id' => $seriesNo,
				'date_added' => $now,
				'type' => $type,
				'project_id' => $task->project_id,
				'ticket_id' => $task->ticket_id
			]);

		return Response::json(['message' => 'success']);
		
	}

	/**
     * Create a task.
     *
     * @return Json
     */
	public function createTask() {

		$ticketId 		= Request::input('ticket_id');
		$projectId 		= Request::input('project_id');
		$description 	= Request::input('description');
		$fromUser 		= Request::input('from_user');
		$toUsers 		= Request::input('to_users');
		$type	 		= Request::input('type');
		$dateDue 		= date('Y-m-d H:i:s', strtotime(Request::input('date_due')));
		$now			= date('Y-m-d H:i:s');

		$orgId = DB::table('user_subscriptions')
			->select('object_id')
			->where('user_id', $fromUser)
			->where('object', 'organization')
			->first()
			->object_id;
		
		if (count($toUsers) > 1) {
			$tempArr = [];
			foreach ($toUsers as $value) {
				$tempArr[] = ['user_id' => $value, 'type' => 1];
			}
			$toUsers = serialize($tempArr);
		} else {
			$toUsers = $toUsers[0];
		}

		if (!isset($dateDue)) {
			$dateDue = $now;
		}

		$ticket = DB::table('tickets')
			->where('id', $ticketId)
			->first();

		$nextSeriesNo = $ticketId * 100;

		if ($temp = DB::table('ticket_logs')
			->select('series_no')
			->where('ticket_id', $ticketId)
			->whereNull('parent_id')
			->latest('date_added')
			->first()
			->series_no) {
			
			$nextSeriesNo += ($temp - $nextSeriesNo) + 1;
			
		}

		DB::table('ticket_logs')
			->insert([
				'org_id' => $orgId,
				'ticket_id' => $ticketId,
				'ticket_type_id' => $ticket->ticket_type_id,
				'description' => $description,
				'from_user' => $fromUser,
				'to_users' => $toUsers,
				'status' => 'New',
				'type' => $type,
				'date_due' => $dateDue,
				'date_added' => $now,
				'date_modified' => $now,
				'series_no' => $nextSeriesNo,
				'is_sync_to_bit' => 0,
				'project_id' => $ticket->project_id,
				'private_note_type' => 0,
				'response_type' => 0,
				'modified_by' => $fromUser
			]);

		return Response::json(['message' => 'success']);
		
	}

	/**
     * Search in task
     *
     * @param  int $offset
     * @param  String $searchValue
     * @return Json
     */
	public function search($offset, $userId, $searchValue) {

		$tasks = $this->taskQueryBuilder($offset, $userId);

		$tasks->where(function($query) use ($searchValue) {

			$query->where('task.status', 'like', '%' . $searchValue . '%')
				->orWhere('task.type', 'like', '%' . $searchValue . '%')
				->orWhere('project.name', 'like', '%' . $searchValue . '%')
				->orWhere('task.description', 'like', '%' . $searchValue . '%');

		});

		$tasks = $tasks->get();
		$this->customizeTasks($tasks);
		return Response::json($tasks);

	}

	/**
     * Get tasks comments.
     *
     * @param  int $seriesNo
     * @return Json
     */
	public function getComments($seriesNo) {
		
		$comments = DB::table( 'ticket_logs as comments' )
			->select(
				'comments.id',
				'comments.from_user',
				'comments.description',
				'comments.parent_id',
				'comments.date_added' )
			->where('comments.parent_id', '=', $seriesNo)
			->latest('date_added')
			->get();
		
		foreach ($comments as $comment) {
			$comment->from_user = $this->getUser($comment->from_user);
		}

		return Response::json($comments);
		
	}

	/**
     * Delete comment.
     *
     * @return Json
     */
	public function deleteComment() {

		$id 	= Request::input('id');
		$now	= date('Y-m-d H:i:s');

		$parentId = DB::table('ticket_logs')
			->select('parent_id')
			->where('id', $id)
			->first()
			->parent_id;

		// Delete comment
		DB::table( 'ticket_logs' )
			->where('id', $id)
			->delete();

		// Update task modified date
		DB::table('ticket_logs')
			->where('series_no', $parentId)
			->update([
				'date_modified' => $now
			]);

		return Response::json(['message' => 'Success']);
		
	}

	/**
     * Edit comment.
     *
     * @return Json
     */
	public function editComment() {

		$id				= Request::input('id');
		$comment 		= Request::input('comment');
		$now 			= date('Y-m-d H:i:s');

		$parentId = DB::table('ticket_logs')
			->select('parent_id')
			->where('id', $id)
			->first()
			->parent_id;

		// Change comment
		DB::table( 'ticket_logs' )
			->where('id', $id)
			->update([
				'description' => $comment,
				'date_modified' => $now
			]);

		// Update task modified date
		DB::table('ticket_logs')
			->where('series_no', $parentId)
			->update([
				'date_modified' => $now
			]);

		return Response::json(['message' => 'Success']);

	}

	/**
     * Add comment.
     *
     * @return Json
     */
	public function addComment() {

		$seriesNo		= Request::input('parent_sn');
		$userId 		= Request::input('user_id');
		$comment 		= Request::input('comment');
		$status			= Request::input('status');
		$now 			= date('Y-m-d H:i:s');

		// Get other data from parent
		$task = DB::table('ticket_logs')
			->where('series_no', $seriesNo)
			->first();

		// Insert comment
		DB::table( 'ticket_logs' )
			->insert([
				'from_user' => $userId,
				'description' => $comment,
				'parent_id' => $seriesNo,
				'date_added' => $now,
				'status' => $status,
				'project_id' => $task->project_id,
				'ticket_id' => $task->ticket_id
			]);

		// Update task modified date
		DB::table('ticket_logs')
			->where('series_no', $seriesNo)
			->update([
				'date_modified' => $now
			]);

		return Response::json(['message' => 'Success']);

	}

	/**
     * Fix dates in tasks using the user timezone.
     *
     * @param  Mixed $tasks
     * @param  int $userId
     * @return Mixed
     */
	private function fixDates($tasks, $userId) {

		$timezone = DB::table('users')
			->leftJoin('zone', 'zone.zone_id', 'users.zone_id')
			->select('zone.zone_name')
			->where('users.id', $userId)
			->first()
			->zone_name;

		foreach ($tasks as $task) {

			if (isset($task->date_added)) {
				$dt = DateTime::createFromFormat('Y-m-d H:i:s', $task->date_added);
				$dt->setTimezone(new DateTimeZone($timezone));
				$task->date_added = $dt->format('Y-m-d H:i:s');
			}

			if (isset($task->date_modified)) {
				$dt = DateTime::createFromFormat('Y-m-d H:i:s', $task->date_modified);
				$dt->setTimezone(new DateTimeZone($timezone));
				$task->date_modified = $dt->format('Y-m-d H:i:s');
			}

		}

	}

	/**
     * Customize the tasks.
     *
     * @param  Mixed $tasks
     * @return Mixed
     */
	private function customizeTasks($tasks) {

		/*
		* Change the value of to_users into array of users instead of id
		* reducing the compability problem of serialized data
		* from php to java
		* then get the count of comments
		*/

		foreach ($tasks as $task) {

			if (!is_numeric($task->to_users)) {

				# if there are many assignee

				$arr = [];

				$task->to_users = unserialize($task->to_users);

				foreach ($task->to_users as $tempArr) {
					$arr[] = $this->getUser($tempArr['user_id']);
				}

				$task->to_users = $arr;

			} else {

				# if it is single assignee

				$user = $this->getUser($task->to_users);
				$task->to_users = [$user];

			}

			$task->from_user = $this->getUser ($task->from_user);

			# Number of comments
			if ($numberOfComments = DB::table( 'ticket_logs as comment' )
				->select(DB::raw('count(comment.id) as number_of_comments'))
				->where('parent_id', '=', $task->series_no)
				->first()
				->number_of_comments) {
				$task->number_of_comments = $numberOfComments;
			} else {
				$task->number_of_comments = 0;
			}

		}

	}

	/**
     * Customize the tasks.
     *
     * @param  int $userid
     * @return Object
     */
	private function taskQueryBuilder($offset, $userId) {

		$orgId = DB::table('user_subscriptions')
			->select('object_id')
			->where('user_id', $userId)
			->where('object', 'organization')
			->first()
			->object_id;

		$tasks = DB::table( 'ticket_logs as task' )
			->leftJoin('tickets as ticket', 'ticket.id', 'task.ticket_id')
			->leftJoin('projects as project', 'project.id', 'ticket.project_id')
			->select(
				'task.id',
				'task.parent_id',
				'task.ticket_id',
				'task.series_no',
				'task.date_added',
				'task.date_due',
				'task.date_modified',
				'task.description',
				'task.private_note',
				'task.attachments',
				'task.status',
				'task.type',
				'task.from_user',
				'task.to_users',
				'task.project_id',
				'ticket.title as ticket_title',
				'project.name as project_name')
			->where([
				['task.org_id', '=', $orgId],
				['task.type', '!=', 'Comment'],
				['task.type', '!=', 'comment']
			])
			->whereNotNull('task.from_user')
			->whereNotNull('task.to_users')
			->whereNotNull('task.project_id')
			->latest( 'task.date_added' )
			->offset( $offset )
			->limit( 5 );

		return $tasks;

	}

}
