<?php

namespace App\Http\V1;

use App\Http\Controllers\Controller;
use DateTime;
use DateTimeZone;
use DB;
use Response;
use Request;

class TicketController extends Controller {

	/**
     * Get tickets by offset.
     *
     * @param  int $userid
     * @return Json
     */
	public function getTickets($offset, $projectId) {

		$tickets = DB::table('tickets as ticket')
			->leftJoin('ticket_status as status', 'status.id', 'ticket.ticket_status_id')
			->leftJoin('ticket_priorities as priority', 'priority.id', 'ticket.ticket_priority_id')
			->select(
				'ticket.id',
				'ticket.title',
				'ticket.description',
				'ticket.owner_id',
				'ticket.created_by',
				'ticket.date_added',
				'ticket.date_modified',
				'ticket.date_due',
				'status.name as status',
				'priority.name as priority'
			)
			->where('ticket.project_id', $projectId)
			->where('is_deleted', '0')
			->offset( $offset )
			->limit( 5 )
			->latest('date_last_activity')
			->get();

		$project = DB::table('projects')
			->where('id', $projectId)
			->first();

		$subs = DB::table('user_subscriptions')
			->select('user_id')
			->where('object_id', $project->id)
			->where('object', 'project')
			->whereNull('date_unsubscribed')
			->get();
		
		$users = [];

		foreach ($subs as $sub) {
			if ($temp = $this->getUser($sub->user_id)) {
				$users[] = $temp;
			}
		}

		$project->project_members = $users;

		foreach ($tickets as $ticket) {
			$ticket->assignee = $this->getUser($ticket->owner_id);
			$ticket->creator = $this->getUser($ticket->created_by);
			$ticket->project = $project;
			unset($ticket->owner_id);
			unset($ticket->created_by);
		}

		return Response::json($tickets);

	}

	/**
     * Get ticket by id.
     *
     * @param  int $ticketId
     * @return Json
     */
	public function getTicketById($userId, $ticketId) {

		$ticket = DB::table('tickets as ticket')
			->leftJoin('ticket_status as status', 'status.id', 'ticket.ticket_status_id')
			->leftJoin('ticket_priorities as priority', 'priority.id', 'ticket.ticket_priority_id')
			->select(
				'ticket.id',
				'ticket.title',
				'ticket.description',
				'ticket.owner_id',
				'ticket.created_by',
				'ticket.date_added',
				'ticket.date_modified',
				'ticket.date_due',
				'status.name as status',
				'priority.name as priority'
			)
			->where('ticket.id', $ticketId)
			->first();

		$ticket->assignee = $this->getUser($ticket->owner_id);
		$ticket->creator = $this->getUser($ticket->created_by);

		unset($ticket->owner_id);
		unset($ticket->created_by);

		$timezone = DB::table('users')
			->leftJoin('zone', 'zone.zone_id', 'users.zone_id')
			->select('zone.zone_name')
			->where('users.id', $userId)
			->first()
			->zone_name;

		if (isset($ticket->date_added)) {

			$dt = DateTime::createFromFormat('Y-m-d H:i:s', $ticket->date_added);
			$dt->setTimezone(new DateTimeZone($timezone));
			$ticket->date_added = $dt->format('Y-m-d H:i:s');

		}

		if (isset($ticket->date_modified)) {

			$dt = DateTime::createFromFormat('Y-m-d H:i:s', $ticket->date_modified);
			$dt->setTimezone(new DateTimeZone($timezone));
			$ticket->date_modified = $dt->format('Y-m-d H:i:s');

		}

		if (isset($ticket->date_due)) {

			$dt = DateTime::createFromFormat('Y-m-d H:i:s', $ticket->date_due);
			$dt->setTimezone(new DateTimeZone($timezone));
			$ticket->date_due = $dt->format('Y-m-d H:i:s');

		}

		return Response::json($ticket);

	}

	/**
     * Search tickets
     *
     * @return Json
     */
	public function search($userId, $offset, $searchValue) {

		$orgId = DB::table('user_subscriptions')
			->select('object_id')
			->where('user_id', $userId)
			->where('object', 'organization')
			->first()
			->object_id;

		$tickets = DB::table('tickets as ticket')
			->leftJoin('ticket_status as status', 'status.id', 'ticket.ticket_status_id')
			->leftJoin('ticket_priorities as priority', 'priority.id', 'ticket.ticket_priority_id')
			->select(
				'ticket.id',
				'ticket.title',
				'ticket.description',
				'ticket.owner_id',
				'ticket.created_by',
				'ticket.date_added',
				'ticket.date_modified',
				'ticket.date_due',
				'ticket.project_id',
				'status.name as status',
				'priority.name as priority'
			)
			->where('ticket.is_deleted', '0')
			->where('ticket.org_id', $orgId)
			->where('ticket.project_id', '!=', 0)
			->whereNotNull('ticket.project_id')
			->where(function($query) use ($searchValue) {
				$query->where('ticket.title', 'like', '%' . $searchValue . '%')
					->orWhere('ticket.description', 'like', '%' . $searchValue . '%');
			})
			->offset( $offset )
			->limit( 5 )
			->latest('date_last_activity')
			->get();

		foreach ($tickets as $ticket) {
			
			$project = DB::table('projects')
				->where('id', $ticket->project_id)
				->first();

			$subs = DB::table('user_subscriptions')
				->select('user_id')
				->where('object_id', $ticket->project_id)
				->where('object', 'project')
				->whereNull('date_unsubscribed')
				->get();

			$users = [];

			foreach ($subs as $sub) {
				if ($temp = $this->getUser($sub->user_id)) {
					$users[] = $temp;
				}
			}

			if (count($users) > 0) {
				$project->project_members = $users;
			}

			$ticket->assignee = $this->getUser($ticket->owner_id);
			$ticket->creator = $this->getUser($ticket->created_by);
			$ticket->project = $project;
			unset($ticket->owner_id);
			unset($ticket->created_by);
			unset($ticket->project_id);

		}
		
		return Response::json($tickets);

	}

	/**
     * Create ticket for a project
     *
     * @return Json
     */
	public function createProjectTicket() {

		# Ticket Types
		# 1 Internal
		# 2 Support
		# 3 Order
		# 4 Project <-

		$typeId = 4;

		$projectId 		= Request::input('project_id');
		$userId 		= Request::input('user_id');
		$categoryId 	= Request::input('category_id');
		$title 			= Request::input('title');
		$description 	= Request::input('description');
		$assignee		= Request::input('assignee');

		$dateAdded 		= date('Y-m-d H:i:s');
		$dateStart 		= date('Y-m-d H:i:s');
		$lastActivity 	= date('Y-m-d H:i:s');

		$project = DB::table('projects')
			->where('id', $projectId)
			->first();
		
		$sequenceNo = DB::table('tickets')
			->select('sequence_no')
			->orderBy('sequence_no', 'desc')
			->limit(1)
			->first()
			->sequence_no + 1;

		$seriesNo = DB::table('tickets')
			->select('series_no')
			->orderBy('series_no', 'desc')
			->limit(1)
			->first()
			->series_no + 1;

		$statusId = DB::table('ticket_status')
			->select('id')
			->where('org_id', $project->org_id)
			->where(function($query) use ($typeId) {
				$query->where('exclusive_to_type', $typeId)
					->orWhereNull('exclusive_to_type');
			})
			->first()
			->id;

		DB::table('tickets')
			->insert([
				'org_id' => $project->org_id,
				'ticket_type_id' => $typeId,
				'project_id' => $projectId,
				'account_id' => $project->account_id,
				'category_id' => $categoryId,
				'title' => $title,
				'description' => $description,
				'ticket_source_id' => $project->default_source_id,
				'progress' => 0,
				'is_combined' => 0,
				'combination_type' => 0,
				'ticket_priority_id' => 0,
				'ticket_severity_id' => 0,
				'ticket_status_id' => $statusId,
				'is_deleted' => 0,
				'date_added' => $dateAdded,
				'date_start' => $dateStart,
				'created_by' => $userId,
				'owner_id' => $assignee,
				'sequence_no' => $sequenceNo,
				'series_no' => $seriesNo,
				'last_activity_by' => $userId,
				'date_last_activity' => $lastActivity
			]);

		return Response(['message' => 'success']);

	}

	/**
     * Create ticket for a project
     *
     * @return Json
     */
	public function getTicketCategories($userId) {

		$orgId = DB::table('user_subscriptions')
			->select('object_id')
			->where('user_id', $userId)
			->where('object', 'organization')
			->first()
			->object_id;

		$categories = DB::table('ticket_categories')
			->where('org_id', $orgId)
			->where('apply_to_project', 1)
			->get();

		return Response::json($categories);

	}


}