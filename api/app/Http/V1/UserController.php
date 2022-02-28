<?php

namespace App\Http\Controllers\V1;

use App\Http\Controllers\Controller;
use App\Libraries\ShaHash\SHAHasher;
use Illuminate\Support\Facades\Auth;
use DB;
use Response;
use Request;

class UserController extends Controller {

	/**
     * Get user by its credentials. Username and Password
     *
     * @param  String $username
     * @param  String $password
     * @return Json
     */
	public function getUserByCredentials($username, $password) {

		$sha1_hasher = new SHAHasher();

		$user = DB::table( 'users' )
			->leftJoin('user_subscriptions as us', 'us.user_id', '=', 'users.id')
			->leftJoin('employee', 'users.id', 'employee.user_id')
			->leftJoin('organizations', 'organizations.id', 'us.object_id')
			->leftJoin('user_meta as meta1', 'meta1.user_id', '=', 'users.id')
			->leftJoin('user_meta as meta2', 'meta2.user_id', '=', 'users.id')
			->leftJoin('user_meta as meta3', 'meta3.user_id', '=', 'users.id')
			->select('users.id', 'employee.id as employee_id','organizations.id as organization_id', 'organizations.name as organization_name', 'users.image_path', 'meta1.meta_value as firstname', 'meta2.meta_value as lastname', 'meta3.meta_value as email')
			->where([
				['us.object', '=', 'organization'],
				['users.user_name', '=', $username],
				['users.password', '=', $sha1_hasher->make($password)],
				['meta1.meta_key', '=', 'first_name'],
				['meta2.meta_key', '=', 'last_name'],
				['meta3.meta_key', '=', 'email']
			])
			->first();

		return Response::json($user);

	}

	/**
     * Get user by id.
     *
     * @param  int $id
     * @return Json
     */
	public function getUserById($id) {

		$user = DB::table( 'users' )
			->select( '*' )
			->where('id', '=', $id)
			->first();

		return Response::json($user);

	}

	/**
     * Get all user
     *
     * @param  int $userId
     * @return Json
     */
	public function getAllUser($userId) {

		$orgId = DB::table('user_subscriptions')
			->select('object_id')
			->where('user_id', $userId)
			->where('object', 'organization')
			->first()
			->object_id;

		$user = DB::table( 'users as user' )
			->leftJoin('user_subscriptions', 'user_subscriptions.user_id', '=', 'user.id')
			->leftJoin('user_meta as meta1', 'meta1.user_id', '=', 'user.id')
			->leftJoin('user_meta as meta2', 'meta2.user_id', '=', 'user.id')
			->leftJoin('user_meta as meta3', 'meta3.user_id', '=', 'user.id')
			->select('user.id', 'user.image_path', 'meta1.meta_value as firstname', 'meta2.meta_value as lastname', 'meta3.meta_value as email')
			->where([
				['user_subscriptions.object', 'organization'],
				['meta1.meta_key', 'first_name'],
				['meta2.meta_key', 'last_name'],
				['meta3.meta_key', 'email']
			])
			->get();

		return Response::json($user);

	}

	/**
     * Logout for user accounts.
     * 
     * @return Json
     */
	public function logout() {
        Request::user()->token()->revoke();
        return Response::json(['message' => 'success']);
    }

}