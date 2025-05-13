package cat.option.impl;

import cat.Const;
import cat.option.IOption;
import cat.option.OptionContext;

/**
 * 行末に {@link Const#VISIBLE_LINE_END} を表示します
 */
public class ShowEndsOption implements IOption {

    /**
     * 行末に {@link Const#VISIBLE_LINE_END} を追加して返します
     *
     * @param text    対象文字列
     * @param context オプション間で共有する情報
     * @return 変換後文字列
     */
    @Override
    public String transform(String text, OptionContext context) {
        return text.replaceAll("(\r?\n)", Const.LITERAL_LINE_END + "$1");
    }
}
