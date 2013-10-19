package toonviewer;

import java.io.IOException;
import java.nio.ByteBuffer;

class TexAnimation
{

    AnimatedVec3D trans;
    AnimatedVec3D rot;
    AnimatedVec3D scale;

    TexAnimation()
    {
        trans = rot = scale = null;
    }

    void read(ByteBuffer buf)
        throws IOException
    {
        trans = new AnimatedVec3D();
        rot = new AnimatedVec3D();
        scale = new AnimatedVec3D();
        trans.read(buf);
        rot.read(buf);
        scale.read(buf);
    }
}
