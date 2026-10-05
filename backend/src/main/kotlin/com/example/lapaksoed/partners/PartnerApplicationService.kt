package com.example.lapaksoed.partners

import com.example.lapaksoed.auth.User
import com.example.lapaksoed.auth.UserRepository
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import org.springframework.web.server.ResponseStatusException
import java.time.Instant

@Service
class PartnerApplicationService(
    private val partnerApplicationRepository: PartnerApplicationRepository,
    private val userRepository: UserRepository,
) {
    @Transactional
    fun apply(user: User, request: CreatePartnerApplicationRequest): PartnerApplicationResponse {
        val userId = requireNotNull(user.id)
        partnerApplicationRepository.findByUser_Id(userId)?.let { existing ->
            throw ResponseStatusException(
                HttpStatus.CONFLICT,
                "Pengajuan mitra Anda sudah tercatat dengan status ${existing.status}",
            )
        }
        val now = Instant.now()
        return partnerApplicationRepository.save(
            PartnerApplication(
                user = userRepository.getReferenceById(userId),
                businessName = request.businessName.trim(),
                contactPhone = request.contactPhone.trim(),
                description = request.description.trim(),
                status = PartnerApplicationStatus.PENDING,
                createdAt = now,
                updatedAt = now,
            ),
        ).toResponse()
    }

    @Transactional(readOnly = true)
    fun mine(user: User): PartnerApplicationResponse? =
        partnerApplicationRepository.findByUser_Id(requireNotNull(user.id))?.toResponse()
}
