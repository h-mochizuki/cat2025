package cat.option.impl;

import cat.option.OptionContext;
import cat.Cat;
import cat.common.ManifestUtil;
import cat.exception.CancelException;
import cat.option.IOption;

/**
 * バージョンを表示します
 */
public class VersionOption implements IOption {

    // バージョンフォーマット
    private static final String VERSION_FORM = "%s (JDK: %s) %s";

    /**
     * バージョンを取得して処理をキャンセルさせます
     */
    @Override
    public void init(OptionContext context) {
        throw new CancelException(
                String.format(VERSION_FORM, Cat.class.getSimpleName(), ManifestUtil.buildJdk(), ManifestUtil.version()));
    }
}
