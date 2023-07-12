package plugin.enemydown.command;

import java.util.List;
import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.PlayerInventory;

public class EnemyDownCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(sender instanceof Player player){

      //共通
      World world = player.getWorld();



      //初期化
      initPlayerStatus(player);

      //ゲーム終了 ゲーム開始時に退避したアイテムを戻す
      //inventory.setHelmet(helmet);

      //DAY13 敵が出現
      world.spawnEntity(getEnemySpawnLocation(player, world), getEnemy());
    }
    return false;
  }

  /**
   * ゲームを始める前にプレイヤーの状態を設定する。
   * 体力と空腹度を最大にして、装備はネザーライト一色になる。
   *
   * @param player  コマンドを実行したプレイヤー
   */
  private static void initPlayerStatus(Player player) {
    //DAY12 開始時にプレイヤーのステータスを変更する
    //体力と空腹度を最大値にする
    player.setHealth(20);
    player.setFoodLevel(20);

    //DAY14 ゲームスタート時の装備変更
    PlayerInventory inventory = player.getInventory();
    //今持っている装備を退避
    //ItemStack helmet = inventory.getHelmet();
    //装備5点をダイヤ装備に変更
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
  private static EntityType getEnemy() {
    List<EntityType> enemyList = List.of(EntityType.ZOMBIE , EntityType.SKELETON);
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
    //playerの位置座標を取得
    Location playerLocation = player.getLocation();

    //ランダム値生成クラスSplittableRandom nextIntで値を制限 100=0〜99
    // 0だとまずいなら+1,マイナス値を出したい時は-20とかにする
    int randomX = new SplittableRandom().nextInt(20) - 10;
    int randomZ = new SplittableRandom().nextInt(20) - 10;

    double x = playerLocation.getX() + randomX;
    double y = playerLocation.getY();
    double z = playerLocation.getZ() + randomZ;
    //random値出力
    //System.out.println("----------");
    System.out.println("randomX: " + randomX + "randomZ: " + randomZ + "です");
    return new Location(world,x,y,z);
  }
}
