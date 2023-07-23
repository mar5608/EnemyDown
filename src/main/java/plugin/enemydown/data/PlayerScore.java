package plugin.enemydown.data;

// 複数の項目を一つのデータとして扱いたい(DAY16 1:40)
// 情報管理したい時Dataオブジェクトがわかりやすいように作ることが多々ある
// ex ユーザ情報（氏名、電話...)

import lombok.Getter;
import lombok.Setter;
import org.bukkit.entity.Player;

/**
 * EnemyDownのゲームを実行する際のスコア情報を扱うオブジェクト。
 * プレイヤー名、合計点数、日時などの情報を持つ。
 */

// getter/setterを生成するよりbuild.gradleに追加ソースコードして
// lombokを使えるようにして @getter,@setterするのが良い
@Getter
@Setter
public class PlayerScore {
  private String playerName;
  private int score;

  // ↑publicにすることも可能だけど現場のお作法的には(DAY16 8:29)
  // privateのまま
  // 右クリック 生成 getterとsetter
  /*
  public String getPlayerName() {
    return playerName;
  }

  public void setPlayerName(String playerName) {
    this.playerName = playerName;
  }

  public int getScore() {
    return score;
  }

  public void setScore(int score) {
    this.score = score;
  }

   */
}
