package com.johnnylee.cloud.framework.common.core;

import com.google.common.base.Objects;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Key Value 的键值对
 *
 * @author Johnny
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class KeyValue<K, V> {

    private K key;
    private V value;

}
