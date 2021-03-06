/*
 * JBoss, Home of Professional Open Source
 *
 * Copyright 2012 Red Hat, Inc. and/or its affiliates.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.xnio.nativeimpl;

import java.io.IOException;

/**
 * @author <a href="mailto:david.lloyd@redhat.com">David M. Lloyd</a>
 */
abstract class NativeDescriptor {

    final NativeWorkerThread thread;

    final int fd;
    int state;
    int id;

    protected NativeDescriptor(final NativeWorkerThread thread, final int fd) {
        this.thread = thread;
        this.fd = fd;
    }

    public NativeXnioWorker getWorker() {
        return thread.getWorker();
    }

    void setId(int id) {
        this.id = id;
    }

    void unregister() {
        thread.unregister(this);
    }

    void preClose() throws IOException {
        Native.testAndThrow(Native.dup2(Native.DEAD_FD, fd));
    }

    void close() throws IOException {
        Native.testAndThrow(Native.close(fd));
    }

    protected abstract void handleReadReady();

    protected abstract void handleWriteReady();

    public String toString() {
        return getClass().getName() + " id=" + id + " fd=" + fd;
    }
}
