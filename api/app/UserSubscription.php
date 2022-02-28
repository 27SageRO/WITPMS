<?php

namespace App;

use App\Scopes\ActiveScope;
use Illuminate\Database\Eloquent\Model;

class UserSubscription extends Model {

    protected $table = 'user_subscriptions';

    /**
     * Scope a query to only include organization.
     *
     * @param \Illuminate\Database\Eloquent\Builder $query
     * @return \Illuminate\Database\Eloquent\Builder
     */
    public function scopeOrganization($query) {
        return $query->where('object', 'organization');
    }

     /**
     * Scope a query to only include organization by user id.
     *
     * @param \Illuminate\Database\Eloquent\Builder $query
     * @param mixed $type
     * @return \Illuminate\Database\Eloquent\Builder
     */
    public function scopeOfUserOrganization($query, $userId) {
        return $query->where('object', 'organization')
            ->where('user_id', $userId);
    }
    
}