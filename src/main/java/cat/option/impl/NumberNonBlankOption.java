package cat.option.impl;

import cat.option.IOption;
import cat.option.OptionContext;

/**
 * 空行では行番号を表示しないようにします
 */
public class NumberNonBlankOption implements IOption {

    /**
     * 空行で行番号表示をスキップする設定を有効にします
     */
    @Override
    public void init(OptionContext context) {
        context.setSkipLineNumberOnEmptyLine(true);
    }
}
