<?php

/*
|--------------------------------------------------------------------------
| API V2 Routes
|--------------------------------------------------------------------------
| 
| Controllers can be found in app/Http/V2
| Routes below are used for Android WITTY PMS app v2
|
*/

$app->get('test', 'Controller@test');

//======================================================================
// USER
//======================================================================

$app->group(['prefix' => 'user'], function() use ($app) {
	$app->get('/', 'UserController@getUsers');
	$app->get('/mention', 'UserController@getMentionedUser');
	$app->get('/meta', 'UserController@getMeta');
	$app->get('/access', 'UserController@getUserByAccess');
	$app->get('/username', 'UserController@getUserByAUsername');
	$app->get('/{id}', 'UserController@getUserById');
});

//======================================================================
// PROJECT
//======================================================================

$app->group(['prefix' => 'project'], function() use ($app) {
	$app->get('/', 'ProjectController@getProjects');
	$app->get('/name', 'ProjectController@getProjectByName');
});

//======================================================================
// COMMENT
//======================================================================

$app->group(['prefix' => 'comment'], function() use ($app) {
	$app->get('/', 'CommentController@comments');
	$app->post('/', 'CommentController@store');
});

//======================================================================
// FEED
//======================================================================

$app->group(['prefix' => 'feed'], function() use ($app) {
	$app->get('/', 'FeedController@getFeed');
});

//======================================================================
// PROJECT
//======================================================================

$app->group(['prefix' => 'mention'], function() use ($app) {
	$app->get('/hashtag', 'MentionController@hashtag');
});