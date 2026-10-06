<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# PII via toString() Leak Companion Changelog

## [Unreleased]

### Added

- A description page for the inspection in **Settings | Editor |
  Inspections**, which showed "Under construction".

### Changed

- The rating prompt's local counter keeps one-way fingerprints of findings
  instead of their file paths, and deletes the list that earlier versions
  kept.
- `PRIVACY.md` describes the values the plugin keeps in the IDE's local
  settings.

## [0.1.1]

### Fixed

- Review/star CTA now links to this plugin's own Marketplace
  reviews page instead of the vendor's generic plugin list.

## [0.1.0]

### Added

- Warning on a logging/print call passed an instance of a class whose
  `toString()` exposes a PII field -- unlike explicit field selection,
  the field never appears textually at the call site.
- Covers both hand-written/IDE-generated `toString()` and Lombok's
  `@ToString` (respecting `@ToString.Exclude`), plus implicit string
  concatenation.

[Unreleased]: https://github.com/GapHunterLabs/pii-tostring-leak-companion/compare/0.1.1...HEAD
[0.1.1]: https://github.com/GapHunterLabs/pii-tostring-leak-companion/compare/0.1.0...0.1.1
[0.1.0]: https://github.com/GapHunterLabs/pii-tostring-leak-companion/commits/0.1.0
