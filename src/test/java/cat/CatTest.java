package cat;

import static org.junit.jupiter.api.Assertions.*;

import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * {@link Cat#concatenate(String...)}のテスト
 */
class CatTest {

    @DisplayName("ファイル出力のテスト")
    @Nested
    class FileTest {

        @DisplayName("存在しないファイル")
        @Test
        void notfound() throws Exception {
            String notfound = "notfound.txt";
            TestHelper.monitor((out, err) -> {
                assertFalse(new Cat().concatenate(notfound), "エラーになること");
                assertEquals("", out.read(), "標準出力には何も表示されないこと");
                assertLinesMatch(Stream.of(notfound + ": No such file or directory"),
                        err.output(), "エラーが表示されること");
            });
        }

        @DisplayName("存在しないファイルが含まれる場合")
        @Test
        void notfound2() throws Exception {
            String notfound = "notfound.txt";
            String input = "This is test.";
            TestHelper.monitor((out, err) -> TestHelper.text(input, file -> {
                assertFalse(new Cat().concatenate(file, notfound), "エラーになること");
                assertEquals(input, out.read(), "ファイル内容が読み込めること");
                assertLinesMatch(Stream.of(notfound + ": No such file or directory"),
                        err.output(), "エラーが表示されること");
            }));
            TestHelper.monitor((out, err) -> TestHelper.text(input, file -> {
                assertFalse(new Cat().concatenate(notfound, file), "エラーになること");
                assertEquals(input, out.read(), "ファイル内容が読み込めること");
                assertLinesMatch(Stream.of(notfound + ": No such file or directory"),
                        err.output(), "エラーが表示されること");
            }));
        }

        @DisplayName("--の後にオプションが含まれる場合")
        @Test
        void notfound3() throws Exception {
            String option = "-v";
            String input = "This is test.";
            TestHelper.monitor((out, err) -> TestHelper.text(input, file -> {
                assertFalse(new Cat().concatenate(file, "--", option), "エラーになること");
                assertEquals(input, out.read(), "ファイル内容が読み込めること");
                assertLinesMatch(Stream.of(option + ": No such file or directory"),
                        err.output(), "エラーが表示されること");
            }));
        }

        @DisplayName("単一行ファイル(1つ)")
        @Test
        void one_line1() throws Exception {
            String input = "This is test.";
            TestHelper.monitorOut(out -> TestHelper.text(input, file -> {
                assertTrue(new Cat().concatenate(file), "正常終了すること");
                assertEquals(input, out.read(), "ファイル内容が読み込めること");
            }));
        }

        @DisplayName("単一行ファイル(2つ)")
        @Test
        void one_line2() throws Exception {
            String i1 = "This is test1.";
            String i2 = "This is test2.";
            TestHelper.monitorOut(out -> TestHelper.text(i1, f1 -> TestHelper.text(i2, f2 -> {
                assertTrue(new Cat().concatenate(f1, f2), "正常終了すること");
                assertEquals(i1 + i2, out.read(), "ファイル内容が読み込めること");
            })));
        }

        @DisplayName("複数行ファイル(1つ)")
        @Test
        void multi_line1() throws Exception {
            String input = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった
                    """;
            TestHelper.monitorOut(out -> TestHelper.text(input, file -> {
                assertTrue(new Cat().concatenate(file), "正常終了すること");
                assertEquals(input, out.read(), "ファイル内容が読み込めること");
            }));
        }

        @DisplayName("複数行ファイル(3つ)")
        @Test
        void multi_line3() throws Exception {
            String i1 = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった
                    """;
            String i2 = """
                    おばあさんが川で洗濯をしていると
                    大きな桃が
                    　どんぶらこっこ
                    　　どんぶらこっこと
                    """;
            String i3 = """
                    下流に流れていったそうな
                                        
                    おしまい
                    """;
            TestHelper.monitorOut(out -> TestHelper.text(i1, f1 -> TestHelper.text(i2, f2 -> TestHelper.text(i3, f3 -> {
                assertTrue(new Cat().concatenate(f1, f2, f3), "正常終了すること");
                assertEquals(i1 + i2 + i3, out.read(), "ファイル内容が読み込めること");
            }))));
        }
    }

    @DisplayName("標準出力のテスト")
    @Nested
    class StandardInputTest {

        @DisplayName("引数なし")
        @Test
        void no_param() throws Exception {
            String input = "This is test.";
            TestHelper.monitorOut(out -> TestHelper.type(input, () -> {
                assertTrue(new Cat().concatenate(), "正常終了すること");
                assertEquals(input, out.read(), "引数がない場合は標準出力内容が表示される");
            }));
        }

        @DisplayName("'-' 1つ")
        @Test
        void one_param() throws Exception {
            String input = "This is test.";
            TestHelper.monitorOut(out -> TestHelper.type(input, () -> {
                assertTrue(new Cat().concatenate("-"), "正常終了すること");
                assertEquals(input, out.read(), "'-'の場合は標準出力内容が表示される");
            }));
        }

        @DisplayName("'-' 2つ")
        @Test
        void two_params() throws Exception {
            String input = "This is test.";
            TestHelper.monitorOut(out -> TestHelper.type(input, () -> {
                assertTrue(new Cat().concatenate("-", "-"), "正常終了すること");
                assertEquals(input, out.read(), "'-'が複数あっても1度だけしか表示されない");
            }));
        }

        @DisplayName("'--' の後に '-' 1つ")
        @Test
        void as_file() throws Exception {
            String input = "This is test.";
            TestHelper.monitorOut(out -> TestHelper.type(input, () -> {
                assertTrue(new Cat().concatenate("--", "-"), "正常終了すること");
                assertEquals(input, out.read(), "'-'の場合は標準出力内容が表示される");
            }));
        }
    }

    @DisplayName("オプションのテスト")
    @Nested
    class OptionTest {

        @DisplayName("存在しないオプション")
        @Test
        void no_such_option() throws Exception {
            TestHelper.monitorOut(out -> {
                assertThrows(RuntimeException.class,
                        () -> assertTrue(new Cat().concatenate("--no-such-option")));
                assertEquals("", out.read(), "標準出力には何も表示されないこと");
            });
        }

        @DisplayName("バージョン")
        @Test
        void version() throws Exception {
            TestHelper.monitorOut(out -> {
                assertTrue(new Cat().concatenate("--version"));
                assertLinesMatch(Stream.of("Cat \\(JDK: .*\\) under construction$"), out.output(),
                        "バージョンが表示される");
            });
            TestHelper.monitorOut(out -> {
                assertTrue(new Cat().concatenate("--version", "--help"));
                assertLinesMatch(Stream.of("Cat \\(JDK: .*\\) under construction$"), out.output(),
                        "先に指定されたため、バージョンが表示される");
            });
        }

        @DisplayName("ヘルプ")
        @Test
        void help() throws Exception {
            TestHelper.monitorOut(out -> {
                assertTrue(new Cat().concatenate("--help"));
                assertLinesMatch(Stream.of("Usage: cat4j [OPTION]... [FILE]...", ">> >>"),
                        out.output(), "ヘルプが表示される");
            });
            TestHelper.monitorOut(out -> {
                assertTrue(new Cat().concatenate("--help", "--version"));
                assertLinesMatch(Stream.of("Usage: cat4j [OPTION]... [FILE]...", ">> >>"),
                        out.output(), "先に指定されたためヘルプが表示される");
            });
        }

        @DisplayName("末尾表示")
        @Test
        void show_ends() throws Exception {
            String input = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった
                    """;
            TestHelper.monitorOut(out -> TestHelper.text(input, file -> {
                assertTrue(new Cat().concatenate("-E", file), "正常終了すること");
                assertLinesMatch(Stream.of(input.split(Const.LINE_SEPARATOR)).map(l -> l + "\\$"),
                        out.output(), "末尾に $ がつくこと");
            }));
        }

        @DisplayName("タブ表示")
        @Test
        void show_tabs() throws Exception {
            String input = """
                    if now.hour >= 12 and now.hour < 16:
                    \tprint("Hello, world")
                    else:
                    \tprint("Good-by")
                    """;
            String expect = """
                    if now.hour >= 12 and now.hour < 16:
                    ^Iprint("Hello, world")
                    else:
                    ^Iprint("Good-by")
                    """;
            TestHelper.monitorOut(out -> TestHelper.text(input, file -> {
                assertTrue(new Cat().concatenate("-T", file), "正常終了すること");
                assertEquals(expect, out.read(), "");
            }));
        }

        @DisplayName("行番号表示")
        @Test
        void number() throws Exception {
            String input = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった
                                        
                    そんなこんなでいろいろあってな
                                        
                                        
                                        
                    おしまい
                    """;
            String expect = """
                    \s    1  昔々あるところにおじいさんとおばあさんがおったそうな
                         2  今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった
                         3 \s
                         4  そんなこんなでいろいろあってな
                         5 \s
                         6 \s
                         7 \s
                         8  おしまい
                    """;
            TestHelper.monitorOut(out -> TestHelper.text(input, file -> {
                assertTrue(new Cat().concatenate("-n", file), "正常終了すること");
                assertEquals(expect, out.read(), "行番号がつくこと");
            }));
        }

        @DisplayName("行番号表示(空行除く)")
        @Test
        void number_non_blank() throws Exception {
            String input = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった

                    おしまい
                    """;
            String expect = """
                    \s    1  昔々あるところにおじいさんとおばあさんがおったそうな
                         2  今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった

                         3  おしまい
                    """;
            TestHelper.monitorOut(out -> TestHelper.text(input, file -> {
                assertTrue(new Cat().concatenate("-b", file), "正常終了すること");
                assertEquals(expect, out.read(), "行番号がつくこと");
            }));
        }

        @DisplayName("空行をまとめる")
        @Test
        void squeeze_blank() throws Exception {
            String input = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった

                    そんなこんなでいろいろあってな



                    おしまい
                    """;
            String expect = """
                    昔々あるところにおじいさんとおばあさんがおったそうな
                    今日もおじいさんは山へ芝刈りに、おばあさんは川へ洗濯にいった

                    そんなこんなでいろいろあってな

                    おしまい
                    """;
            TestHelper.monitorOut(out -> TestHelper.text(input, file -> {
                assertTrue(new Cat().concatenate("-s", file), "正常終了すること");
                assertEquals(expect, out.read(), "空行がまとまること");
            }));
        }
    }

}