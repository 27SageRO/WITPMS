<?php

namespace App;

use App\Scopes\ActiveScope;
use Illuminate\Database\Eloquent\Model;

class Project extends Model {

	protected $hidden = [
        'org_id',
        'account_id',
        'template_id',
        'user_group_id',
        'project_type',
        'status',
        'is_active',
        'start_date',
        'end_date',
        'date_modified',
        'created_by',
        'modified_by',
        'default_source_id',
        'bg_image',
        'visibility'
    ];

    public function creator() {
        return $this->belongsTo(User::class, 'created_by');
    }

    protected static function boot() {
        parent::boot();
        static::addGlobalScope(new ActiveScope);
    }
    
}