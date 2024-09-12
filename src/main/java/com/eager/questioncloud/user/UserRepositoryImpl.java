package com.eager.questioncloud.user;

import static com.eager.questioncloud.creator.QCreatorEntity.creatorEntity;
import static com.eager.questioncloud.user.QUserEntity.userEntity;

import com.eager.questioncloud.creator.CreatorEntity;
import com.eager.questioncloud.exception.CustomException;
import com.eager.questioncloud.exception.Error;
import com.eager.questioncloud.user.UserDto.UserWithCreator;
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
        return userJpaRepository.findByEmail(email)
            .orElseThrow(() -> new CustomException(Error.FAIL_LOGIN))
            .toDomain();
    }

    @Override
    public User getUser(Long uid) {
        return userJpaRepository.findById(uid)
            .orElseThrow(() -> new CustomException(Error.NOT_FOUND))
            .toDomain();
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
            return new UserWithCreator(user.toDomain(), creator.toModel());
        }
        
        return new UserWithCreator(user.toDomain(), null);
    }

    @Override
    public User append(User user) {
        return userJpaRepository.save(user.toEntity()).toDomain();
    }

    @Override
    public User save(User user) {
        return userJpaRepository.save(user.toEntity()).toDomain();
    }

    @Override
    public Optional<User> getSocialUser(AccountType accountType, String socialUid) {
        Optional<UserEntity> userEntity = userJpaRepository.findByAccountTypeAndSocialUid(accountType, socialUid);
        return userEntity.map(UserEntity::toDomain);
    }

    @Override
    public Boolean checkDuplicatePhone(String phone) {
        return userJpaRepository.existsByPhone(phone);
    }

    @Override
    public Boolean checkDuplicateEmail(String email) {
        return userJpaRepository.existsByEmail(email);
    }
}
