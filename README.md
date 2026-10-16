# Shield-MITE

为 Minecraft 1.6.4-MITE / FishModLoader 添加原版风格盾牌。盾牌支持举盾、正面格挡、耐久损耗、近战击退和斧类破盾，不支持旗帜图案。安装 Offhand-MITE 后，盾牌也可放在副手使用；该模组不是必需依赖。

## 使用

使用盾牌时按住右键。举盾 5 tick 后，盾牌能完全挡住来自正前方且没有被 MITE 标记为不可格挡的伤害；伤害达到 3 点时会消耗盾牌耐久。攻击者使用斧类武器命中格挡中的盾牌后，盾牌会被禁用 5 秒。

盾牌最大耐久为 336，使用木板和铁锭按原版形状合成：

```text
木板 铁锭 木板
木板 木板 木板
  木板
```

## 第三方模组 API

在 `IGameRegistry.onGameRegistry()` 中创建盾牌并通过 `ShieldRegistry` 注册。注册名必须包含命名空间。物品图标名对应 `assets/<namespace>/textures/items/<path>.png`，盾牌模型贴图使用资源标识符，例如 `examplemod:textures/models/shield.png`。

```java
private static final MinecraftRegistry REGISTRY =
    new MinecraftRegistry("examplemod").initAutoItemRegister();

public static ShieldItem SHIELD;

@Override
public void onGameRegistry() {
    SHIELD = ShieldRegistry.register(
        REGISTRY,
        "examplemod:shield",
        "examplemod.shield",
        new ShieldItem(
            500,
            "examplemod:shield",
            "examplemod:textures/models/shield.png",
            Item.ingotIron,
            Material.iron));
}
```

`ShieldItem` 完整构造器参数依次为耐久、物品图标名、盾牌模型贴图、修理物品和修理材料。便捷构造器 `new ShieldItem(durability, iconTexture, repairItem, repairMaterial)` 会使用 Shield-MITE 的无图案模型贴图；`new ShieldItem(durability, iconTexture)` 还会默认使用木板修理。自定义盾牌的配方由注册它的模组添加。

## 构建

```text
./gradlew build
```
