<?php

namespace App\Http\V2;

use Laravel\Lumen\Routing\Controller as BaseController;
use Illuminate\Http\Request;
use App\User;
use App\UserSubscription;
use App\Project;
use App\Ticket;
use App\TicketCategory;
use DB;

class MentionController extends Controller {

	/**
     * Hashtag Mention
     *
     * @param  Request $request
     * @return Response
     */
    public function hashtag(Request $request) {

        $value = '%' . $request->get('value') . '%';
        $offset = $request->get('page') * 5;
    	$orgId = UserSubscription::ofUserOrganization($request->user()->id)->first()->object_id;

    	$projects = Project::where('org_id', $orgId)
    		->where('name', 'LIKE', $value)
            ->orWhere('short_name', 'LIKE', $value)
            ->orderBy('date_created', 'ASC')
            ->limit(5);

    	$tickets = Ticket::select('tickets.*', DB::raw('CONCAT(projects.short_name, "-", tickets.series_no) as generated_id'))
            ->where('tickets.org_id', $orgId)
    		->where(DB::raw('CONCAT(projects.short_name, "-", tickets.series_no)'), 'LIKE', $value)
            ->orWhere('tickets.title', 'LIKE', $value)
    		->leftJoin('projects', 'projects.id', 'tickets.project_id')
            ->orderBy('tickets.date_last_activity', 'ASC')
            ->limit(5);

        $ticketCategories = TicketCategory::where('name', 'LIKE', $value)
            ->limit(5);

        if (isset($offset)) {
            $projects->offset($offset);
            $tickets->offset($offset);
            $ticketCategories->offset($offset);
        }

        $tickets = $tickets->get();
        $projects = $projects->get();
        $ticketCategories = $ticketCategories->get();

        foreach ($projects as $project) {
            $project->creator;
        }

        foreach ($tickets as $ticket) {
            $ticket->assignees;
            $ticket->project;
            $ticket->category;
        }

        return response()->json([
            'projects' => $projects,
            'tickets' => $tickets,
            'ticket_categories' => $ticketCategories
        ]);

    }

}