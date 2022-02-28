<?php

namespace App;

use Illuminate\Database\Eloquent\Model;

class UserMeta extends Model {

	protected $table = 'user_meta';
 	
    public function scopeOnly($q, $value) {
        return $q->whereIn('meta_key', $value);
    }

}