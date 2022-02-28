<?php

namespace App\Http\V2;

use Laravel\Lumen\Routing\Controller as BaseController;
use Illuminate\Http\Request;
use App\User;
use App\UserSubscription;
use App\Project;

class ProjectController extends Controller {


	/**
     * Get all project by user organization.
     *
     * @param  Request $request
     * @return Response
     */
    public function getProjects(Request $request) {

    	$orgId = UserSubscription::ofUserOrganization($request->user()->id)->first()->object_id;

    	$projects = Project::where('org_id', $orgId)->get();

    	foreach ($projects as $project) {
    		$project->creator;
    	}

        return response()->json($projects);

    }

    /**
     * Get all project by user organization and project name.
     *
     * @param  Request $request
     * @return Response
     */
    public function getProjectByName(Request $request) {

    	$orgId = UserSubscription::ofUserOrganization($request->user()->id)->first()->object_id;

    	$projects = Project::where('org_id', $orgId)
    		->where('name', 'LIKE', '%' . $request->get('value') . '%')
    		->get();

    	foreach ($projects as $project) {
    		$project->creator;
    	}

        return response()->json($projects);

    }


}