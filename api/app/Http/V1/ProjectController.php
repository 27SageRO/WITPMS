<?php

namespace App\Http\V1;

use App\Http\Controllers\Controller;
use DB;
use Response;
use Request;

class ProjectController extends Controller {

	/**
     * Get projects by user id
     *
     * @param  int $id
     * @return Json
     */
	public function getProjectsByUserId($userId) {

		$orgId = DB::table('user_subscriptions')
			->select('object_id')
			->where('user_id', $userId)
			->where('object', 'organization')
			->first()
			->object_id;

		$projects = DB::table('projects')
			->where('org_id', $orgId)
			->orderBy('name', 'asc')
			->get();

		foreach ($projects as $project) {
			
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

		}

		return Response::json($projects);

	}

	/**
     * Get projects by id
     *
     * @param  int $projectId
     * @return Json
     */
	public function getProjectById($projectId) {

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

		return Response::json($project);

	}

	/**
     * Search projects
     *
     * @param  int $userId
     * @param  String $searchValue
     * @return Json
     */
	public function search($userId, $searchValue) {

		$orgId = DB::table('user_subscriptions')
			->select('object_id')
			->where('user_id', $userId)
			->where('object', 'organization')
			->first()
			->object_id;

		$projects = DB::table('projects')
			->where('org_id', $orgId)
			->where(function($query) use ($searchValue) {
				$query->where('name', 'like', '%' . $searchValue . '%')
					->orWhere('short_name', 'like', '%' . $searchValue . '%');
			})
			->get();

		foreach ($projects as $project) {
			
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

		}

		return Response::json($projects);

	}

}