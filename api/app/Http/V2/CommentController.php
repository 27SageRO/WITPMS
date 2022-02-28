<?php

namespace App\Http\V2;

use Laravel\Lumen\Routing\Controller as BaseController;
use Illuminate\Http\Request;
use App\Comment;
use App\Task;
use DB;

class CommentController extends Controller {

	const TYPE = 'Comment';

	/**
     * Get comments by task series number
     *
     * @param  Request $request
     * @return Response
     */
    public function comments(Request $request) {

    	$taskSeriesNumber = $request->get('task_series_no');

    	$comments = Comment::commentFrom($taskSeriesNumber)
    		->get();

    	foreach ($comments as $comment) {
    		$comment->creator->fillAttributes();
    	}

    	return response()->json($comments);

    }

    /**
     * Store a comment
     *
     * @param  Request $request
     * @return Response
     */
    public function store(Request $request) {

		$now = date('Y-m-d H:i:s');

		$parent = Task::where('series_no', $request->task_series_no)
			->first();

		$comment = new Comment;
		$comment->parent_id = $request->task_series_no;
		$comment->from_user = $request->user_id;
		$comment->description = $request->description;
		$comment->project_id = $parent->project_id;
		$comment->ticket_id = $parent->ticket_id;
		$comment->type = self::TYPE;
		$comment->save();
		
		// $parent->date_modified = $now;
		$parent->save();

		return response()->json(['message' => 'Success']);

    }


}