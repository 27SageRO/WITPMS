<?php

/*
|--------------------------------------------------------------------------
| API V1 Routes
|--------------------------------------------------------------------------
| 
| Controllers can be found in app/Http/V1
| Routes below are used for Android WITTY PMS app v1
|
*/

$app->get('test', 'TestController@test');

//======================================================================
// USER
//======================================================================
$app->post('user/logout', 'UserController@logout');
$app->get('user/all/{userId}', 'UserController@getAllUser');
$app->get('user/{username}/{password}', 'UserController@getUserByCredentials');
$app->get('user/{id}', 'UserController@getUserById');

//======================================================================
// TASK & TASK COMMENTS
//======================================================================

$app->post('task/filter', 'TaskController@filter');

$app->post('task', 'TaskController@createTask');
$app->delete('task', 'TaskController@deleteTask');
$app->patch('task/type', 'TaskController@updateTaskType');
$app->patch('task/status', 'TaskController@updateTaskStatus');
$app->patch('task/due', 'TaskController@updateTaskDue');
$app->patch('task/assignees', 'TaskController@updateTaskAssignees');
$app->patch('task/description', 'TaskController@updateTaskDescription');

$app->get('task/search/{offset}/{userId}/{searchValue}', 'TaskController@search');

$app->get('task/comment/{seriesNo}', 'TaskController@getComments');
$app->post('task/comment', 'TaskController@addComment');
$app->patch('task/comment', 'TaskController@editComment');
$app->delete('task/comment', 'TaskController@deleteComment');

$app->get('task/byid/{userId}/{taskId}', 'TaskController@getTaskById');

$app->get('task/{offset}/{userId}', 'TaskController@getTasks');
$app->get('task/{offset}/{userId}/{ticketId}', 'TaskController@getTasksByTicket');

//======================================================================
// Projects
//======================================================================
$app->get('project/search/{userId}/{searchValue}', 'ProjectController@search');
$app->get('project/byid/{projectId}', 'ProjectController@getProjectById');
$app->get('project/{userId}', 'ProjectController@getProjectsByUserId');

//======================================================================
// TICKETS
//======================================================================
$app->post('ticket/project', 'TicketController@createProjectTicket');
$app->get('ticket/search/{userId}/{offset}/{searchValue}', 'TicketController@search');
$app->get('ticket/category/{userId}', 'TicketController@getTicketCategories');
$app->get('ticket/byid/{userId}/{ticketId}', 'TicketController@getTicketById');
$app->get('ticket/{offset}/{projectId}', 'TicketController@getTickets');