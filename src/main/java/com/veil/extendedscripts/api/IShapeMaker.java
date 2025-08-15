package com.veil.extendedscripts.api;

/**
 * Provides a variant of methods for creating shapes.
 * TAKE NOTE: This class is a work in progress. Pyramid and cone shapes are known to have issues.
 */
public interface IShapeMaker {
    public IPos[] getBox(IPos center, int width, int length, int height);

    public IPos[] getBox(IPos pos1, IPos pos2);

    public IPos[] getHollowBox(IPos center, int width, int length, int height, double thickness);

    public IPos[] getHollowBox(IPos center, int width, int length, int height);

    public IPos[] getHollowBox(IPos pos1, IPos pos2, double thickness);

    public IPos[] getHollowBox(IPos pos1, IPos pos2);

    public IPos[] getEllipsoid(IPos center, int radius);

    public IPos[] getEllipsoid(IPos center, int sizeX, int sizeY, int sizeZ);

    public IPos[] getHollowEllipsoid(IPos center, int radius);

    public IPos[] getHollowEllipsoid(IPos center, int radius, double thickness);
;

    public IPos[] getHollowEllipsoid(IPos center, int sizeX, int sizeY, int sizeZ, double thickness);

    public IPos[] getHollowEllipsoid(IPos center, int sizeX, int sizeY, int sizeZ);

    public IPos[] getCylinder(IPos center, int radius, int height);

    public IPos[] getCylinder(IPos pos1, IPos pos2);

    public IPos[] getHollowCylinder(IPos center, int radius, int height);

    public IPos[] getHollowCylinder(IPos center, int radius, int height, double thickness);

    public IPos[] getHollowCylinder(IPos pos1, IPos pos2);

    public IPos[] getHollowCylinder(IPos pos1, IPos pos2, double thickness);

    public IPos[] getPyramid(IPos center, int baseWidth, int baseLength, int height);

    public IPos[] getPyramid(IPos pos1, IPos pos2);

    public IPos[] getHollowPyramid(IPos center, int baseWidth, int baseLength, int height, double thickness);

    public IPos[] getHollowPyramid(IPos center, int baseWidth, int baseLength, int height);

    public IPos[] getCone(IPos center, int baseRadius, int height);

    public IPos[] getCone(IPos pos1, IPos pos2);

    public IPos[] getHollowCone(IPos center, int baseRadius, int height, double thickness);

    public IPos[] getHollowCone(IPos center, int baseRadius, int height);
}
