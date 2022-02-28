<?php

namespace App;

use Illuminate\Database\Eloquent\Model;
use Illuminate\Database\Eloquent\Builder;

class TicketCategory extends Model {

	protected $table = 'ticket_categories';

	protected $hidden = [
		'icon',
		'apply_to_support',
		'apply_to_project',
		'position',
		'org_id',
		'parent_id'
	];

	protected static function boot() {
        parent::boot();
        static::addGlobalScope('apply', function (Builder $builder) {
            $builder->where('apply_to_project', 1);
        });
    }
    
}