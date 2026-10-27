package com.college.placement.config;

import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.context.properties.bind.DefaultValue;
import org.springframework.validation.annotation.Validated;

/**
 * TECH TALK: Type-Safe Configuration Properties (@ConfigurationProperties)
 * Immutable, constructor-bound Java 21 record.
 * Automatically validated at startup via @Validated and Jakarta Bean Validation constraints.
 */
@ConfigurationProperties(prefix = "placement")
@Validated
public record PlacementProperties(
        @Valid @NotNull EmailProperties email,
        @Valid @NotNull PolicyProperties policy,
        @Valid @NotNull JwtProperties jwt,
        @Valid @NotNull StorageProperties storage
) {

    public record EmailProperties(
            @NotBlank @DefaultValue("college.edu.in") String allowedDomain
    ) {}

    public record PolicyProperties(
            @Positive @Min(1) @DefaultValue("72") int offerResponseWindowHours,
            @Positive @DefaultValue("10.0") double dreamCtcThresholdLpa,
            @DefaultValue("true") boolean blockRegularAfterRegularOffer,
            @DefaultValue("true") boolean dreamAllowedAfterRegular
    ) {}

    public record JwtProperties(
            @Positive @DefaultValue("15") int accessTokenMinutes,
            @Positive @DefaultValue("7") int refreshTokenDays,
            @NotBlank String secret
    ) {}

    public record StorageProperties(
            @DefaultValue("true") boolean enabled,
            @NotBlank @DefaultValue("./target/storage-demo") String baseDir,
            @Positive @DefaultValue("2097152") long maxResumeSizeBytes
    ) {}

    public PlacementProperties withMaskedSecret() {
        return new PlacementProperties(
                this.email,
                this.policy,
                new JwtProperties(this.jwt.accessTokenMinutes(), this.jwt.refreshTokenDays(), "******** [MASKED]"),
                this.storage
        );
    }
}

