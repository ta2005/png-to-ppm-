# PNG Decoder Project

## Quick Commands
* **Compile:** `:make`
* **Run:** `:!make run` *(or `:make run` depending on your keybind)*

## Roadmap (TODO)
- [ ] **Finish the Parser:** Implement the Finite State Machine to enforce chunk ordering and add CRC validation.
- [ ] **add IDAT filters :** impleents all 7 of the png filters
- [ ] **Address Interlacing:** Implement the Adam7 algorithm to decode interlaced images.
