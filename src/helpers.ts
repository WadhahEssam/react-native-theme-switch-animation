export const validateCoordinates = (
  value: number,
  max: number,
  name: string
) => {
  if (value === undefined) {
    console.warn(`${name} is undefined. Please provide both cx and cy.`);
    return false;
  }
  if (value > max) {
    console.warn(
      `${name} is greater than ${max}. Please provide a ${name} smaller than screen size.`
    );
    return false;
  }
  if (value < -max) {
    console.warn(
      `${name} is smaller than -${max}. Please provide a ${name} bigger than -screen size.`
    );
    return false;
  }
  return true;
};

export const calculateRatio = (value: number, max: number) => {
  return value >= 0 ? value / max : 1 + value / max;
};

export const calculateActualRation = (ration: number) => {
  return ration > 0 ? ration : 1 + ration;
};
