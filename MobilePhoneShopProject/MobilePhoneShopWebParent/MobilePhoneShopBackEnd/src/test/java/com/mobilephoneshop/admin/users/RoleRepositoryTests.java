package com.mobilephoneshop.admin.users;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import com.mobilephoneshop.admin.user.RoleRepository;
import com.mobilephoneshop.common.entity.Role;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback(false)
public class RoleRepositoryTests
{
	@Autowired
	private RoleRepository roleRepository;

	@Test
	public void testCreateFirstRole()
	{
		Role roleAdmin = new Role("Admin", "Có thể thực hiện được mọi thao tác");
		Role saveRole = roleRepository.save(roleAdmin);

		assertThat(saveRole.getId()).isGreaterThan(0);
	}

	@Test
	public void testCreateRestRoles()
	{
		Role roleSalesPerson = new Role("Nhân viên bán hàng",
				"Có thể quản lý giá, khách hàng, vận chuyển hàng, đơn đặt hàng và báo cáo bán hàng");
		Role roleEditor = new Role("Quản lý kho", "Có thể quản lý loại sản phẩm, hãng, sản phẩm, bài viết và menu");
		Role roleShipper = new Role("Shipper", "Có thể xem sản phẩm, xem đơn hàng, update trạng thái đơn hàng");
		Role roleAssistant = new Role("Tư vấn viên", "Có thể quản lý các câu hỏi và review");
		roleRepository.saveAll(List.of(roleSalesPerson, roleEditor, roleShipper, roleAssistant));
	}
}
