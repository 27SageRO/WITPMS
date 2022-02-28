<?php

namespace App;

use App\Scopes\UndeletedScope;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class Ticket extends Model {

	protected $hidden = [
        'org_id',  
        'old_id',  
        'ticket_type_id',  
        'unique_id',
        'account_id',  
        'category_id',  
        'subcategory_id',  
        'project_id',  
        'project_header_id',  
        'project_tree_sort',  
        'project_milestone_id',  
        'components_id',
        'ticket_source_id', 
        'ticket_priority_id',  
        'ticket_severity_id',  
        'ticket_support_type_id',  
        'configuration_id',  
        'ticket_status_id',  
        'is_deleted',      
        'created_by',  
        'modified_by',  
        'order_id',  
        'closing_notes',
        'date_closed',          
        'progress',  
        'is_combined',  
        'master_ticket_id',  
        'combination_type',  
        'combined_by',  
        'combined_date',  
        'owner_id',
        'ticket_attachments',  
        'ticket_app_involved',  
        'sequence_no', 
        'last_activity_by',
        'email',   
        'to_users',  
        'duration',
    ];

    public function type() {
        return $this->belongsTo(TicketType::class, 'ticket_type_id');
    }

    public function category() {
        return $this->belongsTo(TicketCategory::class, 'category_id');
    }

    public function project() {
        return $this->belongsTo(Project::class, 'project_id');
    }

    public function assignees() {
        return $this->hasMany(User::class, 'id', 'owner_id');
    }

    protected static function boot() {
        parent::boot();
        static::addGlobalScope(new UndeletedScope);
        static::addGlobalScope('ticket_scope', function (Builder $builder) {
            $builder->where('ticket_type_id', 4);
        });
    }
    
}