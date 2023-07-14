package plugin.enemydown;

import org.bukkit.Bukkit;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;
import plugin.enemydown.command.EnemyDownCommand;


/*アクセス修飾子 class クラス名 extends 継承元のクラス名{
   サブクラスの定義
}*/
// JavaPluginを継承したEnemyDownクラスを公開する(public)
// クラス名は先頭大文字、ファイル名.javaと一致する(初級編DAY13)
// publicはアクセス修飾子という。publicはありとあらゆるところから見れる(初級編DAY13)
// クラスの中にメソッドを書いていく。
// 単一責任の原則。一つのクラスに一つの役割を持たす。(初級編DAY13)

//中級編DAY15でimplements Listenerを消す
//初級編DAY13 14:43〜の説明より
// implements ListenerでListenerを実装。
// Listenerはマーカーインターフェイスと言われて、Listenerというインターフェイスを実装していれば
//   @EventHandlerとアノテーションをつけたものに対して、ここの引数に情報が入ってくるよ。
// 動画15:11〜 Listenerというインターフェイスを実装(implements)してたら、
// @EventHandlerが機能するようになって、@EventHandlerが機能していたら、このイベント情報を持ったメソッドがあった動いていくれる。
//public final class EnemyDown extends JavaPlugin implements Listener{
public final class EnemyDown extends JavaPlugin {

    // 実行された時
    @Override
    public void onEnable() {
        // Plugin startup logic
        // 変数にする
        EnemyDownCommand enemyDownCommand = new EnemyDownCommand();
        //Bukkit.getPluginManager().registerEvents(this, this);
        // 登録されたやつ
        Bukkit.getPluginManager().registerEvents(enemyDownCommand, this);
        // コマンドで"enemyDown"が入力されたら実行する
        // setExecutorでenemyDownCommandクラスを実行する
        getCommand("enemyDown").setExecutor(enemyDownCommand);
    }

}
