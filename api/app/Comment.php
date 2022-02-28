<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;
use DB;

class Comment extends Model {

	const CREATED_AT = 'date_added';
    const UPDATED_AT = 'date_modified';
    
	protected $table = 'ticket_logs';

	protected $hidden = [
		'org_id',  
		'origin_log_id',  
		'parent_id',  
		'reply_to_log',  
		'origin_ticket_id',
		'ticket_id',
		'ticket_type_id',  
		'project_id',
		'private_note',
		'private_note_type',
		'response_type',
		'from_user',
		'to_users',
		'attachments',
		'reference',
		'modified_by',
		'is_voided',
		'has_replies',
		'total_progress',
		'severity_id',
		'type',
		'time_spent',
		'is_sync_to_bit',
		'old_id',
		'old_parent_id',
		'ticket_old_id',
		'old_series_no',
		'is_updated'
	];

	public function creator() {
        return $this->belongsTo(User::class, 'from_user');
    }

    public function scopeCommentFrom($query, $taskSeriesNo) {
    	return $query->where('parent_id', $taskSeriesNo);
    }

    public function scopeOfOrganization($query, $id) {
    	return $query->where('org_id', $id);
    }

    protected static function boot() {
        parent::boot();
        static::addGlobalScope('comment_scope', function (Builder $builder) {
            $builder->where('type', '=', 'Comment');
            $builder->whereNotNull('from_user');
        });
    }

}