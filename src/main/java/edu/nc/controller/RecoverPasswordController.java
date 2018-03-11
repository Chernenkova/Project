package edu.nc.controller;


import edu.nc.service.RecoverPasswordService;
import edu.nc.dataaccess.wrapper.recoverpasswordwrappers.EmailWrapper;
import edu.nc.common.GeneralSettings;
import edu.nc.dataaccess.wrapper.recoverpasswordwrappers.PasswordWrapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping(value = GeneralSettings.RECOVER_PASSWORD)
public class RecoverPasswordController {

    private RecoverPasswordService recoverPasswordService;

    @Autowired
    public RecoverPasswordController(RecoverPasswordService recoverPasswordService) {
        this.recoverPasswordService = recoverPasswordService;
    }

    @RequestMapping(value = "/request", method = RequestMethod.POST)
    public ResponseEntity requestOnRecovering(@RequestBody EmailWrapper wrapper) {
        return recoverPasswordService.requestOnRecovering(wrapper);
    }

    @RequestMapping(value = GeneralSettings.RECOVER_CONFIRM + "/{string}", method = RequestMethod.POST)
    public ResponseEntity recoverPassword(@RequestBody PasswordWrapper wrapper, @PathVariable String string) {
        return recoverPasswordService.recoverPassword(wrapper, string);
    }
}
