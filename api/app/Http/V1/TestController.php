<?php

namespace App\Http\V1;

use Illuminate\Http\Request;

class TestController extends Controller
{
    /**
     * Create a new controller instance.
     *
     * @return void
     */
    public function __construct()
    {
        //
    }


    public function test(Request $request) {
        return $request->user()->id;
    }

}
