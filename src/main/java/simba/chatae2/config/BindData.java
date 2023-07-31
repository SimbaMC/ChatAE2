/* SPDX-License-Identifier: AGPL-3.0 WITH SimbaMC Proxy and SimbaMC Exceptions */
package simba.chatae2.config;

import com.google.common.collect.Maps;

import java.util.Map;
import java.util.OptionalLong;

public class BindData {

    static Map<String, Long> Binding = Maps.newHashMap();

    public static void Bind(String BindKey, Long GridKey) {
        Binding.put(BindKey, GridKey);
    }

    public static OptionalLong Query(String BindKey) {
        if (Binding.containsKey(BindKey)) {
            return OptionalLong.of(Binding.get(BindKey));
        } else {
            return OptionalLong.empty();
        }
    }

}
