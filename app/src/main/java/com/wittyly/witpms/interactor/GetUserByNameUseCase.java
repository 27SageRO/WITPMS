package com.wittyly.witpms.interactor;

import com.wittyly.witpms.model.User;
import com.wittyly.witpms.repository.UserRepository;

import java.util.List;

import javax.inject.Inject;

import io.reactivex.Observable;

public class GetUserByNameUseCase extends UseCase<List<User>, Params> {

    private final UserRepository userRepository;

    @Inject
    public GetUserByNameUseCase(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    Observable<List<User>> buildUseCaseObservable(Params params) {
        return userRepository.getMentions(
                params.getString("name")
        );
    }

}
