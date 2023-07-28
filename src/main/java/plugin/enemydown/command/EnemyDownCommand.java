package plugin.enemydown.command;

import java.net.http.WebSocket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SplittableRandom;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;
import plugin.enemydown.EnemyDown;
import plugin.enemydown.data.PlayerScore;



public class EnemyDownCommand implements CommandExecutor, Listener {

  // DAY17
  private EnemyDown enemyDown;
  private List <PlayerScore> playerScoreList = new ArrayList<>();
  //時間はフィールドにもつ
  // ラムダ式の中で値が変わるものは持てない？
  private  int gameTime = 20;

  // DAY17
  public EnemyDownCommand(EnemyDown enemyDown) {
    this.enemyDown = enemyDown;
  }
  //private Player player;
  //private int score;
  // 上2行だけだと情報を複数持てない(DAY15 18:45)
  // private Map<Player,Integer> scores;

  /**
   * コマンドを実行した時に処理される
   * @param sender コマンドを実行したPlayer情報　Source of the command
   * @param command ？　Command which was executed
   * @param label コマンドを実行したPlayerのレベル情報　Alias of the command which was used
   * @param args Passed command arguments
   * @return コマンド実行playerとplayerが同じならtrueを返す
   */
  // アノテーション（注釈）　コンパイラやJVM向けの情報：特殊なコメント
  // @Override オーバーライド（メソッドの再定義）
  // 「このメソッドはオーバーライドしているメソッドです」とコンパイラに伝えるためのもの
  //  @Overrideアノテーションを付けることで、オーバーライドしていなかった場合、コンパイルエラーを発生させることができる
  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {

    //[検]もしコマンドを実行したplayerと画面の？playerが同じなら
    if(sender instanceof Player player){
      if(playerScoreList.isEmpty()) {
        addNewPlayer(player);
        // thisをつけると最初のprivate Player player;になる
        // DAY16 でコメントアウト this.player = player;
      }else  {
        for(PlayerScore playerScore : playerScoreList){
          // ! notの意味　
          if(!playerScore.getPlayerName().equals(player.getName())){
            addNewPlayer(player);
          }
        }
      }
      gameTime = 20;
      // 共通
      // playerの情報からworld情報を取得し変数に持っておく
      World world = player.getWorld();

      // 初期化
      initPlayerStatus(player);

      // 制限時間
      // ラムダ式
      // delay 時間ずらす 
      // period 間隔 5秒に1回　マイクラ固有1チック20秒 1チックは1秒未満
      Bukkit.getScheduler().runTaskTimer(enemyDown,Runnable -> {
        if(gameTime <= 0){
          // 処理を止める
          Runnable.cancel();
          player.sendMessage("ゲームが終了しました。");
          return;
        }
        world.spawnEntity(getEnemySpawnLocation(player, world), getEnemy());
        gameTime -= 5;
      }, 0, 5 * 20);

      // ゲーム終了 ゲーム開始時に退避したアイテムを戻す
      //inventory.setHelmet(helmet);

      //DAY13 敵が出現
      // spawnEntityでは第1引数にnew Locationが必要
      // DAY17コメントアウト 時間制限の中へ移動
      // world.spawnEntity(getEnemySpawnLocation(player, world), getEnemy());

    }
    return false;
  }

  /**
   * 新規のプレイヤー情報をリストに追加します。
   *
   * @param player コマンドを実行したプレイヤー
   */
  private void addNewPlayer(Player player) {
    PlayerScore playerScore = new PlayerScore();
    //getterとsetterがpublicなので取ってこれる
    playerScore.setPlayerName(player.getName());
    playerScoreList.add(playerScore);
  }

  //EntityDeathEventイベントがあるのでEventHandlerでGetして使う(DAY15 01:01)

  /**
   * コマンドを実行したプレイヤーと敵を倒したプレイヤーが同じなら
   * そのプレイヤーに点数が加算されスコアが表示される。
   * @param e 敵が倒れた情報
   */
  @EventHandler
  public void onEnemyDeath(EntityDeathEvent e){
    // 敵を倒したプレイヤー情報を変数playerに格納
    Player player = e.getEntity().getKiller();
    //  敵を倒していなければplayerがnullになる
    //  コマンドを実行していなければthis.playerがnullになる
    // Nullチェック
    // プレイヤーが敵を倒していない時playerがNullならreturnを返し処理をスキップする
    // プレイヤーがコマンドを実行していない時に敵が倒れたらreturnを返し処理をスキップする
    if(Objects.isNull(player) || playerScoreList.isEmpty()){
      return;
    }
    for(PlayerScore playerScore : playerScoreList){
      if(playerScore.getPlayerName().equals(player.getName())){
        playerScore.setScore(playerScore.getScore()+10);
        player.sendMessage("敵を倒した！現在のスコアは " + playerScore.getScore() + "点！");
      }
    }
    // コマンドを実行したプレイヤーとここのplayer（=敵を倒したプレイヤー）が同じなら
    /* if(this.player.getName().equals(player.getName())){
      score += 10;
      player.sendMessage("敵を倒した！現在のスコアは " + score + "点！");
    }
     */
  }

  /**
   * ゲームを始める前にプレイヤーの状態を設定する。
   * 体力と空腹度を最大にして、装備はネザーライト一色になる。
   *
   * @param player  コマンドを実行したプレイヤー
   */
  // voidは戻り値を戻さないメソッドであることを表す(初級編DAY13)
  private void initPlayerStatus(Player player) {
    //DAY12 開始時にプレイヤーのステータスを変更する
    //体力と空腹度を最大値にする
    player.setHealth(20);
    player.setFoodLevel(20);

    //DAY14 ゲームスタート時の装備変更
    PlayerInventory inventory = player.getInventory();
    //今持っている装備を退避（例）
    //ItemStack helmet = inventory.getHelmet();
    //装備5点をネザーライト装備に変更
    inventory.setHelmet(new ItemStack(Material.NETHERITE_HELMET));
    inventory.setChestplate(new ItemStack(Material.NETHERITE_CHESTPLATE));
    inventory.setLeggings(new ItemStack(Material.NETHERITE_LEGGINGS));
    inventory.setBoots(new ItemStack(Material.NETHERITE_BOOTS));
    inventory.setItemInMainHand(new ItemStack(Material.NETHERITE_SWORD));
  }

  /**
   * ランダムで敵を抽選して、その結果の敵を取得します。
   * @return  敵
   */
  // メソッドで処理した結果を戻したいt場合にはreturnを使う(初級編DAY13)
  // private static 戻り値の型 メソッド名(引数のデータ型 引数の変数名)
  private static EntityType getEnemy() {
    // 敵の情報をリストに登録
    List<EntityType> enemyList = List.of(EntityType.ZOMBIE , EntityType.SKELETON);
    // ランダムでリストから敵を取得してreturnで返す
    return enemyList.get(new SplittableRandom().nextInt(2));
  }

  /**
   * 敵の出現場所を取得します。
   * 出現エリアはX軸とZ軸は自分の位置からプラス、ランダムで-10〜9の値が設定されます。
   * Y軸はプレイヤーと同じ位置になります。
   *
   * 受け取ったプレイヤーの位置情報から敵のスポーン座標を返す
   * @param player  コマンドを実行したプレイヤー
   * @param world コマンドを実行したプレイヤーが所属するワールド
   * @return  敵の出現場所
   */
  private Location getEnemySpawnLocation(Player player, World world) {
    //player.getLocation()でplayerの位置座標を取得
    Location playerLocation = player.getLocation();

    //ランダム値生成クラスSplittableRandom nextIntで値を制限 20 = 0〜19
    // 0だとまずいなら+1,マイナス値を出したい時は-20とかにする
    int randomX = new SplittableRandom().nextInt(20) - 10;
    int randomZ = new SplittableRandom().nextInt(20) - 10;

    // x軸は東西(＋が東)、z軸は南北(＋が南)、y軸は高さ
    double x = playerLocation.getX() + randomX;
    double y = playerLocation.getY();
    double z = playerLocation.getZ() + randomZ;
    //random値出力
    //System.out.println("----------");
    System.out.println("randomX: " + randomX + "　randomZ: " + randomZ + "です");
    return new Location(world,x,y,z);
  }
}
