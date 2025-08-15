package com.veil.extendedscripts.extendedapi;

import noppes.npcs.api.IPos;

/**
 * Provides a variant of methods for creating shapes.
 * TAKE NOTE: This class is a work in progress. Pyramid and cone shapes are known to have issues.
 */
public interface IShapeMaker {
    IPos[] getBox(IPos center, int width, int length, int height);

    IPos[] getBox(IPos pos1, IPos pos2);

    IPos[] getHollowBox(IPos center, int width, int length, int height, double thickness);

    IPos[] getHollowBox(IPos center, int width, int length, int height);

    IPos[] getHollowBox(IPos pos1, IPos pos2, double thickness);

    IPos[] getHollowBox(IPos pos1, IPos pos2);

    IPos[] getEllipsoid(IPos center, int radius);

    IPos[] getEllipsoid(IPos center, int sizeX, int sizeY, int sizeZ);

    IPos[] getHollowEllipsoid(IPos center, int radius);

    IPos[] getHollowEllipsoid(IPos center, int radius, double thickness);
;

    IPos[] getHollowEllipsoid(IPos center, int sizeX, int sizeY, int sizeZ, double thickness);

    IPos[] getHollowEllipsoid(IPos center, int sizeX, int sizeY, int sizeZ);

    IPos[] getCylinder(IPos center, int radius, int height);

    IPos[] getCylinder(IPos pos1, IPos pos2);

    IPos[] getHollowCylinder(IPos center, int radius, int height);

    IPos[] getHollowCylinder(IPos center, int radius, int height, double thickness);

    IPos[] getHollowCylinder(IPos pos1, IPos pos2);

    IPos[] getHollowCylinder(IPos pos1, IPos pos2, double thickness);

    IPos[] getPyramid(IPos center, int baseWidth, int baseLength, int height);

    IPos[] getPyramid(IPos pos1, IPos pos2);

    IPos[] getHollowPyramid(IPos center, int baseWidth, int baseLength, int height, double thickness);

    IPos[] getHollowPyramid(IPos center, int baseWidth, int baseLength, int height);

    IPos[] getCone(IPos center, int baseRadius, int height);

    IPos[] getCone(IPos pos1, IPos pos2);

    IPos[] getHollowCone(IPos center, int baseRadius, int height, double thickness);

    IPos[] getHollowCone(IPos center, int baseRadius, int height);
}
