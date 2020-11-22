package com.ilfalsodemetrio.api.v2;

import com.ilfalsodemetrio.api.v2.beans.Message;

public interface Bot {
    Message ai(Message msg);
}
