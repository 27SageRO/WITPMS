<?php

namespace App\Http\V2;

use Laravel\Lumen\Routing\Controller as BaseController;
use Illuminate\Http\Request;
use App\User;
use App\UserSubscription;
use App\Task;
use DB;

class FeedController extends Controller {

	/**
     * Ticket Feed
     *
     * @param  Request $request
     * @return Response
     */
    public function getFeed(Request $request) {

    	$offset = $request->get('page') * 5;
    	$orgId = UserSubscription::ofUserOrganization($request->user()->id)
    		->first()
    		->object_id;

    	$feed = array();

        $tasks = Task::clean()
        	->ofOrganization($orgId)
        	->offset($offset)
        	->limit(5)
        	->latest('date_added')
        	->get();

    	foreach ($tasks as $task) {

    		if (!isset($task->ticket)) {
    			// if ticket is null
    			// means it is already deleted
    			// or isnt a ticket for project
    			unset($task);
    			continue;
    		}

    		$task->ticket->project;
    		$task->assignees;
    		
    		$task->number_of_comments = 
    			Task::numberOfComments($task->series_no)
	    			->first()
	    			->number_of_comments;

    		$task->creator->fillAttributes();
    		foreach ($task->assignees as $assignee) {
    			$assignee->fillAttributes();
    		}
    		$feed[]['task'] = $task;
    	}

        return response()->json($feed);

    }

}