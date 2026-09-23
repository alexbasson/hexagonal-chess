require 'user_management/user_id'
require 'user_management/friendship_repository'
require 'user_management/remove_friend'

RSpec.describe UserManagement::RemoveFriend do
  subject(:remove_friend) { described_class.new(friendship_repository) }

  let(:friendship_repository) { instance_double(UserManagement::FriendshipRepository) }
  let(:owner_id)              { UserManagement::UserId.new(value: 'u1') }
  let(:friend_id)             { UserManagement::UserId.new(value: 'u2') }

  before { allow(friendship_repository).to receive(:delete_by_owner_and_friend) }

  it 'calls repository delete with owner and friend' do
    remove_friend.call(owner_id, friend_id)

    expect(friendship_repository).to have_received(:delete_by_owner_and_friend).with(owner_id, friend_id)
  end
end
