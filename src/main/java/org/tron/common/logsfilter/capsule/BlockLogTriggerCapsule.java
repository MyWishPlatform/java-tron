package org.tron.common.logsfilter.capsule;

import lombok.Getter;
import lombok.Setter;
import org.tron.common.logsfilter.EventPluginLoader;
import org.tron.common.logsfilter.trigger.BlockLogTrigger;
import org.tron.core.capsule.BlockCapsule;

import java.util.stream.Collectors;

public class BlockLogTriggerCapsule extends TriggerCapsule {
  @Getter
  @Setter
  BlockLogTrigger blockLogTrigger;

  public BlockLogTriggerCapsule(BlockCapsule block) {
    blockLogTrigger = new BlockLogTrigger();
    blockLogTrigger.setBlockHash(block.getBlockId().toString());
    blockLogTrigger.setTimeStamp(block.getTimeStamp());
    blockLogTrigger.setBlockNumber(block.getNum());
    blockLogTrigger.setTransactionSize(block.getTransactions().size());
    block.getTransactions().forEach(trx ->
        blockLogTrigger.getTransactionList().add(trx.getTransactionId().toString())
    );

    blockLogTrigger.setTransactions(
        block.getTransactions()
        .stream()
        .map(trx -> new TransactionLogTriggerCapsule(trx, block))
        .map(TransactionLogTriggerCapsule::getTransactionLogTrigger)
        .collect(Collectors.toList()));
  }

  @Override
  public void processTrigger() {
    EventPluginLoader.getInstance().postBlockTrigger(blockLogTrigger);
  }
}
