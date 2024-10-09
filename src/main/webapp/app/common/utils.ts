import { FormGroup, AbstractControl, ValidationErrors, ValidatorFn } from '@angular/forms';


/**
* Update all controls of the provided form group with the given data.
*/
export function updateForm(group: FormGroup, data: any) {
  for (const field in group.controls) {
    const control = group.get(field)!;
    let value = data[field] === undefined ? null : data[field];
    control.setValue(value);
  }
}

/**
 * Helper function for transforming a Record to a Map to support number as a key.
 */
export function transformRecordToMap(data:Record<number,number|string>):Map<number,string> {
  const dataMap = new Map();
  for (const [key, value] of Object.entries(data)) {
    dataMap.set(+key, '' + value);
  }
  return dataMap;
}
/**
 * Helper function for transforming a object to another object to remove the null properties.
 */
export function transformObjectToAnotherObject(record: any): { [key: string]: string | number | boolean }{
  const result: { [key: string]: string | number | boolean } = {};
  Object.keys(record).forEach((key) => {
    const value = record[key];
    if (value !== null && value !== undefined) {
      result[key] = value;
    }
  });
  return result;
}

export const validDouble: ValidatorFn = (control: AbstractControl): ValidationErrors | null => {
  const valid = control.value === null || /^(-?)[0-9]*(\.[0-9]+)?$/.test(control.value);
  return valid ? null : { validDouble: { value: control.value } };
};
