package dev.gaphunter.piitostringleakcompanion.inspection

import com.intellij.testFramework.fixtures.BasePlatformTestCase

class PiiToStringInspectionTest : BasePlatformTestCase() {

    override fun setUp() {
        super.setUp()
        myFixture.enableInspections(PiiToStringInspection::class.java)
    }

    fun `test logging a user object whose hand-written toString exposes email is flagged`() {
        myFixture.configureByText(
            "UserController.java",
            """
            class User {
                String email;
                @Override
                public String toString() {
                    return "User{email=" + email + "}";
                }
            }

            class UserController {
                private static final Logger log = null;
                void handle(User user) {
                    log.info(user);
                }
            }

            interface Logger {
                void info(Object o);
            }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.any { it.description?.contains("leaks implicitly") == true })
    }

    fun `test logging a user object with Lombok ToString exposing ssn is flagged`() {
        myFixture.configureByText(
            "UserController2.java",
            """
            import lombok.ToString;

            @ToString
            class User2 {
                String ssn;
            }

            class UserController2 {
                private static final Logger log = null;
                void handle(User2 user) {
                    log.warn(user);
                }
            }

            interface Logger {
                void warn(Object o);
            }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.any { it.description?.contains("leaks implicitly") == true })
    }

    fun `test Lombok ToString Exclude on the PII field is not flagged`() {
        myFixture.configureByText(
            "UserController3.java",
            """
            import lombok.ToString;

            @ToString
            class User3 {
                @ToString.Exclude
                String ssn;
            }

            class UserController3 {
                private static final Logger log = null;
                void handle(User3 user) {
                    log.warn(user);
                }
            }

            interface Logger {
                void warn(Object o);
            }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("leaks implicitly") == true })
    }

    fun `test explicit field selection is not flagged`() {
        myFixture.configureByText(
            "UserController4.java",
            """
            class User4 {
                String email;
                @Override
                public String toString() {
                    return "User{email=" + email + "}";
                }
                String getEmail() { return email; }
            }

            class UserController4 {
                private static final Logger log = null;
                void handle(User4 user) {
                    log.info(user.getEmail());
                }
            }

            interface Logger {
                void info(Object o);
            }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("leaks implicitly") == true })
    }

    fun `test a toString that does not reference the PII field is not flagged`() {
        myFixture.configureByText(
            "UserController5.java",
            """
            class User5 {
                String email;
                String name;
                @Override
                public String toString() {
                    return "User{name=" + name + "}";
                }
            }

            class UserController5 {
                private static final Logger log = null;
                void handle(User5 user) {
                    log.info(user);
                }
            }

            interface Logger {
                void info(Object o);
            }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.none { it.description?.contains("leaks implicitly") == true })
    }

    fun `test implicit string concatenation is also flagged`() {
        myFixture.configureByText(
            "UserController6.java",
            """
            class User6 {
                String email;
                @Override
                public String toString() {
                    return "User{email=" + email + "}";
                }
            }

            class UserController6 {
                private static final Logger log = null;
                void handle(User6 user) {
                    log.info("Processing: " + user);
                }
            }

            interface Logger {
                void info(Object o);
            }
            """.trimIndent(),
        )
        val highlights = myFixture.doHighlighting()
        assertTrue(highlights.any { it.description?.contains("leaks implicitly") == true })
    }
}
