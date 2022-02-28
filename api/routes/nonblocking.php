<?php

/*
|--------------------------------------------------------------------------
| Non blocking routes
|--------------------------------------------------------------------------
| 
| These routes doesn't need authorization
|
*/

$app->group(['prefix' => 'user'], function() use ($app) {
	$app->get('/username', 'UserController@getUserByAUsername');
});