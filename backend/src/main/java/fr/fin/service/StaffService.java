package fr.fin.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import fr.fin.model.entity.Staff;
import fr.fin.repository.StaffRepository;

@Service
public class StaffService implements UserDetailsService {

	@Autowired
	private StaffRepository staffRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		return staffRepository.findByUsername(username);
	}

	public List<Staff> getAllStaffs() {
		return staffRepository.findByStatusTrue();
	}

	public Staff createStaff(Staff staff) {

		Staff newStaff = staffRepository.save(staff);
		return newStaff;
	}

	public Staff getStaffById(Integer id) {

		if (staffRepository.findById(id).isPresent()) {
			return staffRepository.findById(id).get();
		}
		return null;
	}
	
	public Staff getStaffByUserName(String userName) {
		if ( staffRepository.findByUsername(userName) != null ) {
			return staffRepository.findByUsername(userName);
		}
		return null;
	}

	public void saveStaff(Staff staff) {
		staffRepository.save(staff);
	}
	
	public Staff updateStaffStatus(Integer id) {
		Staff updatedStaff = this.getStaffById(id);
		if(updatedStaff != null) {
			updatedStaff.setStatus(updatedStaff.isStatus() ? false : true);
			staffRepository.save(updatedStaff);
			return updatedStaff;
		}
		return null;
	}
}
