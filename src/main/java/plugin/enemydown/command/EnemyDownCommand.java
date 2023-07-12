package plugin.enemydown.command;

import java.util.SplittableRandom;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.EntityType;
import org.bukkit.entity.Player;

public class EnemyDownCommand implements CommandExecutor {

  @Override
  public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
    if(sender instanceof Player player){

      //共通
      World world = player.getWorld();

      //開始時に満タンにする
      player.setHealth(20);
      player.setFoodLevel(20);



      //敵が出現
      world.spawnEntity(getEnemySpawnLocation(player, world), EntityType.ZOMBIE);
    }
    return false;
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
    System.out.println("----------");
    System.out.println("randomX: " + randomX + "randomZ: " + randomZ + "です");
    return new Location(world,x,y,z);
  }
}
