package com.eager.questioncloud.core.domain.user.infrastructure.repository

import com.eager.questioncloud.core.domain.user.enums.AccountType
import com.eager.questioncloud.core.domain.user.enums.UserStatus
import com.eager.questioncloud.core.domain.user.infrastructure.entity.QUserEntity.userEntity
import com.eager.questioncloud.core.domain.user.infrastructure.entity.UserEntity.Companion.from
import com.eager.questioncloud.core.domain.user.model.User
import com.eager.questioncloud.core.exception.CoreException
import com.eager.questioncloud.core.exception.Error
import com.querydsl.jpa.impl.JPAQueryFactory
import org.springframework.stereotype.Repository

@Repository
class UserRepositoryImpl(
    private val userJpaRepository: UserJpaRepository,
    private val jpaQueryFactory: JPAQueryFactory,
) : UserRepository {

    override fun getUserByEmail(email: String): User {
        return userJpaRepository.findByUserInformationEntityEmail(email)
            .orElseThrow { CoreException(Error.FAIL_LOGIN) }
            .toModel()
    }

    override fun getUserByPhone(phone: String): User {
        return userJpaRepository.findByUserInformationEntityPhone(phone)
            .filter { entity -> entity.userStatus != UserStatus.Deleted }
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun getUser(uid: Long): User {
        return userJpaRepository.findById(uid)
            .orElseThrow { CoreException(Error.NOT_FOUND) }
            .toModel()
    }

    override fun save(user: User): User {
        return userJpaRepository.save(from(user)).toModel()
    }

    override fun getSocialUser(accountType: AccountType, socialUid: String): User? {
        return userJpaRepository.findByUserAccountInformationEntityAccountTypeAndUserAccountInformationEntitySocialUid(
            accountType,
            socialUid
        )?.toModel()
    }

    override fun checkDuplicatePhone(phone: String): Boolean {
        return userJpaRepository.existsByUserInformationEntityPhone(phone)
    }

    override fun checkDuplicateEmail(email: String): Boolean {
        return userJpaRepository.existsByUserInformationEntityEmail(email)
    }

    override fun checkDuplicateSocialUidAndAccountType(socialUid: String, accountType: AccountType): Boolean {
        return jpaQueryFactory.select(userEntity.uid)
            .from(userEntity)
            .where(
                userEntity.userAccountInformationEntity.socialUid.eq(socialUid),
                userEntity.userAccountInformationEntity.accountType.eq(accountType)
            )
            .fetchFirst() != null
    }

    override fun findByUidIn(ids: List<Long>): List<User> {
        return userJpaRepository.findByUidIn(ids)
            .stream()
            .map { it.toModel() }
            .toList()
    }
}
