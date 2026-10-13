package com.dusty_dusty.meds_and_herbs.mixin;

import com.dusty_dusty.meds_and_herbs.core.ServerLevelRuns;
import net.minecraft.server.level.ServerLevel;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Comparator;
import java.util.Map;
import java.util.PriorityQueue;
import java.util.function.BooleanSupplier;

@Mixin(ServerLevel.class)
public abstract class ServerLevelMixin implements ServerLevelRuns {
    @Unique // Tested for proper ordering
    private static final PriorityQueue<Map.Entry<Integer, Runnable>> medsAndHerbsAgain$scheduledTasks =
            new PriorityQueue<>( Comparator.comparingInt( Map.Entry::getKey) );

    @Inject(method = "tick", at = @At("TAIL"))
    public void medsAndHerbsAgain$tickTask(BooleanSupplier hasTimeLeft, CallbackInfo ci) {
        ServerLevel self = (ServerLevel) (Object) this;
        int currentTick = self.getServer().getTickCount();

        while (!medsAndHerbsAgain$scheduledTasks.isEmpty()
                && medsAndHerbsAgain$scheduledTasks.peek().getKey() <= currentTick
        ) {
            Runnable task = medsAndHerbsAgain$scheduledTasks.poll().getValue();
            if (task != null) task.run();
        }
    }

    @Unique
    @Override
    public void medsAndHerbsAgain$addServerLevelRun(int tickLimit, Runnable run) {
        ServerLevel self = (ServerLevel) (Object) this;
        int executeAtTick = self.getServer().getTickCount() + tickLimit;
        medsAndHerbsAgain$scheduledTasks.offer( Map.entry( executeAtTick, run ) );
    }
}