<?php

namespace App\Http\V2;

use Laravel\Lumen\Routing\Controller as BaseController;
use Illuminate\Http\Request;
use App\User;

class Controller extends BaseController {
	
    // Test Route
	public function test(Request $request) {
        return User::find(47)->meta()->only('first_name')->get();
    }

}