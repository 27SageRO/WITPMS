<?php

namespace App\Http\V2;

use Illuminate\Http\Request;
use App\User;
use DB;

class UserController extends Controller {

    /**
     * Get all user.
     *
     * @param  Request $request
     * @return Response
     */
    public function getUsers(Request $request) {
        $users = User::all();
        foreach ($users as $user) {
            $user->fillAttributes();
        }
        return response()->json($users);
    }

    /**
     * Get specific user by using id.
     *
     * @param  int $id
     * @return Response
     */
    public function getUserById($id) {
        $user = User::find($id);
        if (!$user) {
            return null;
        }
        $user->fillAttributes();
        return response()->json($user);
    }

    /**
     * Get specific user by using his/her access.
     *
     * @param  Request $request
     * @return Response
     */
    public function getUserByAccess(Request $request) {
        $user = User::find($request->user()->id);
        if (!$user) {
            return response()->json(null);
        }
        $user->fillAttributes();
        return response()->json($user);
    }

    /**
     * Get specific user by username.
     *
     * @param  Request $request
     * @return Response
     */
    public function getUserByAUsername(Request $request) {
        $user = User::where('user_name', $request->get('value'))->first();
        if (!$user) {
            return null;
        }
        $user->fillAttributes();
        return response()->json($user);
    }

    /**
     * Get mentioned user.
     *
     * @param  Request $request
     * @return Response
     */
    public function getMentionedUser(Request $request) {

        $users = User::select('users.*')
            ->leftJoin('user_meta as meta1', 'users.id', 'meta1.user_id')
            ->leftJoin('user_meta as meta2', 'users.id', 'meta2.user_id')
            ->groupBy('users.id')
            ->where('meta1.meta_key', 'first_name')
            ->where('meta2.meta_key', 'last_name')
            ->where(DB::raw('CONCAT(meta1.meta_value, " ", meta2.meta_value)'), 'LIKE', '%' . $request->get('value') . '%')
            ->get();

        foreach ($users as $user) {
            $user->fillAttributes();
        }

        return response()->json($users);

    }

    /**
     * Get specific meta value by key.
     *
     * @param  Request $request
     * @return Response
     */
    public function getMeta(Request $request) {
        $key = explode(',', $request->get('key'));
        return response()->json(User::find($request->get('id'))->meta()->only($key)->get());
    }

}