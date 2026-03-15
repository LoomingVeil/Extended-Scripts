export interface IObject {
    getClass(): string;
    toString(): string;
    equals(other: IObject): boolean;
}
