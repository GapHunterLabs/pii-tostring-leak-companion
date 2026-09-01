<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# PII via toString() Leak Companion Changelog

## [Unreleased]

## [0.1.0]

### Added

- Warning on a logging/print call passed an instance of a class whose
  `toString()` exposes a PII field -- unlike explicit field selection,
  the field never appears textually at the call site.
- Covers both hand-written/IDE-generated `toString()` and Lombok's
  `@ToString` (respecting `@ToString.Exclude`), plus implicit string
  concatenation.

[Unreleased]: https://github.com/GapHunterLabs/pii-tostring-leak-companion/compare/0.1.0...HEAD
[0.1.0]: https://github.com/GapHunterLabs/pii-tostring-leak-companion/commits/0.1.0
