<?php

namespace App;

use Laravel\Passport\HasApiTokens;
use App\Libraries\ShaHash\SHAHasher;
use Illuminate\Auth\Authenticatable;
use Laravel\Lumen\Auth\Authorizable;
use Illuminate\Database\Eloquent\Model;
use Illuminate\Contracts\Auth\Authenticatable as AuthenticatableContract;
use Illuminate\Contracts\Auth\Access\Authorizable as AuthorizableContract;

class User extends Model implements AuthenticatableContract, AuthorizableContract
{
    use HasApiTokens, Authenticatable, Authorizable;

    const CREATED_AT = 'date_created';
    const UPDATED_AT = 'date_modified';

    /**
     * The attributes that are mass assignable.
     *
     * @var array
     */
    protected $fillable = [
        'name', 'email',
    ];

    /**
     * The attributes excluded from the model's JSON form.
     *
     * @var array
     */
    protected $hidden = [
        'password',
        'modified_by',
        'api_key',
        'created_by',
        'invite_token',
        'zone_id',
        'level_id',
        'user_type_id'
    ];

    public function findForPassport($username) {
       return self::where('user_name', $username)->first();
    }

    public function fillAttributes() {

        $metas = $this->meta()->only(['first_name', 'last_name'])->get();

        $this->zone;
        $this->level;
        $this->type;
        
        if (count($metas)) {
            $this->first_name = $metas[0]->meta_value;
            $this->last_name = $metas[1]->meta_value;
        }
        
    }

    public function validateForPassportPasswordGrant($password) {
        $hasher = new SHAHasher();
        return $hasher->check($password, $this->getAuthPassword());
    }

    public function type() {
        return $this->belongsTo(UserType::class, 'user_type_id');
    }

    public function level() {
        return $this->belongsTo(Level::class, 'level_id');
    }

    public function zone() {
        return $this->belongsTo(Zone::class, 'zone_id');
    }

    public function role() {
        return $this->belongsTo(UserRole::class, 'role_id');
    }

    public function meta() {
        return $this->hasMany(UserMeta::class, 'user_id');
    }
    
}