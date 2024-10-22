package com.eager.questioncloud.domain.user.repository;

import static com.eager.questioncloud.domain.creator.entity.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.domain.user.entity.QUserEntity.userEntity;

import com.eager.questioncloud.domain.authentication.dto.AuthenticationDto.UserWithCreator;
import com.eager.questioncloud.domain.creator.entity.CreatorEntity;
import com.eager.questioncloud.domain.user.entity.UserEntity;
import com.eager.questioncloud.domain.user.model.User;
import com.eager.questioncloud.domain.user.vo.AccountType;
import com.eager.questioncloud.domain.user.vo.UserStatus;
import com.eager.questioncloud.domain.user.vo.UserType;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.querydsl.core.Tuple;
import com.querydsl.jpa.impl.JPAQueryFactory;
import java.util.Optional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

@Repository
@RequiredArgsConstructor
public class UserRepositoryImpl implements UserRepository {
    private final UserJpaRepository userJpaRepository;
    private final JPAQueryFactory jpaQueryFactory;

    @Override
    public User getUserByEmail(String email) {
        return userJpaRepository.findByUserInformationEmail(email)
            .orElseThrow(() -> new CustomException(Error.FAIL_LOGIN))
            .toModel();
    }

    @Override
    public User getUserByPhone(String phone) {
        return userJpaRepository.findByUserInformationPhone(phone)
            .filter(entity -> !entity.getUserStatus().equals(UserStatus.Deleted))
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public User getUser(Long uid) {
        return userJpaRepository.findById(uid)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toModel();
    }

    @Override
    public UserWithCreator getUserWithCreatorId(Long uid) {
        Tuple result = jpaQueryFactory.select(userEntity, creatorEntity)
            .from(userEntity)
            .leftJoin(creatorEntity).on(creatorEntity.userId.eq(userEntity.uid))
            .where(userEntity.uid.eq(uid))
            .fetchFirst();

        if (result == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        UserEntity user = result.get(userEntity);
        CreatorEntity creator = result.get(creatorEntity);

        if (user == null) {
            throw new CustomException(Error.NOT_FOUND);
        }

        if (user.getUserType().equals(UserType.CreatorUser) && creator != null) {
            return new UserWithCreator(user.toModel(), creator.toModel());
        }

        return new UserWithCreator(user.toModel(), null);
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user.toEntity()).toModel();
    }

    @Override
    public Optional<User> getSocialUser(AccountType accountType, String socialUid) {
        Optional<UserEntity> userEntity = userJpaRepository.findByUserAccountInformationAccountTypeAndUserAccountInformationSocialUid(accountType,
            socialUid);
        return userEntity.map(UserEntity::toModel);
    }

    @Override
    public Boolean checkDuplicatePhone(String phone) {
        return userJpaRepository.existsByUserInformationPhone(phone);
    }

    @Override
    public Boolean checkDuplicateEmail(String email) {
        return userJpaRepository.existsByUserInformationEmail(email);
    }
}
