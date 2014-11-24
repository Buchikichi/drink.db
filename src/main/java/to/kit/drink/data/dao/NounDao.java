package to.kit.drink.data.dao;

import org.springframework.stereotype.Component;

import to.kit.drink.data.dto.Noun;

@Component
public final class NounDao extends DaoBase<Noun> {
	/**
	 * Nounのインスタンス作成.
	 * @param nounId 名詞ID
	 * @param lang 言語コード
	 * @param noun 名詞
	 * @return Nounのインスタンス
	 */
	public static Noun createNoun(String nounId, String lang, String noun) {
		Noun result = new Noun();

		result.setNounId(nounId);
		result.setLang(lang);
		result.setNoun(noun);
		return result;
	}
}
